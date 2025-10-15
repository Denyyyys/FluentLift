package com.denyyyys.fluentLift.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.denyyyys.fluentLift.exceptions.ForbiddenActionException;
import com.denyyyys.fluentLift.exceptions.ResourceAlreadyExistsException;
import com.denyyyys.fluentLift.exceptions.ResourceNotFound;
import com.denyyyys.fluentLift.model.postgres.dto.request.course.CourseCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.ClozeBlockUserProgressWithAnswersResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CourseProgressWithAnswersResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CourseResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.LessonProgressWithAnswersResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.UnitProgressWithAnswersResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.UserCourseEnrollmentResponseDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.entity.course.Course;
import com.denyyyys.fluentLift.model.postgres.entity.course.Lesson;
import com.denyyyys.fluentLift.model.postgres.entity.course.Unit;
import com.denyyyys.fluentLift.model.postgres.entity.course.UserCourseEnrollment;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.ClozeBlockAnswer;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.ClozeBlockUserAnswer;
import com.denyyyys.fluentLift.model.postgres.mapper.CourseMapper;
import com.denyyyys.fluentLift.model.postgres.mapper.UserCourseEnrollmentMapper;
import com.denyyyys.fluentLift.repo.postgres.AppUserRepository;
import com.denyyyys.fluentLift.repo.postgres.ClozeBlockUserAnswerRepository;
import com.denyyyys.fluentLift.repo.postgres.CourseRepository;
import com.denyyyys.fluentLift.repo.postgres.MultipleChoiceBlockRepository;
import com.denyyyys.fluentLift.repo.postgres.MultipleChoiceUserSelectedAnswerRepository;
import com.denyyyys.fluentLift.repo.postgres.UserCourseEnrollmentRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {
        private final CourseRepository courseRepository;
        private final UserCourseEnrollmentRepository enrollmentRepository;
        private final ClozeBlockUserAnswerRepository clozeBlockUserAnswerRepository;
        private final MultipleChoiceBlockRepository multipleChoiceBlockRepository;
        private final MultipleChoiceUserSelectedAnswerRepository multipleChoiceUserSelectedAnswerRepository;

        private final AppUserRepository appUserRepository;

        @Transactional
        public Course createCourse(CourseCreateDto dto, String creatorEmail) {
                Course course = CourseMapper.toEntity(dto);
                AppUser creator = appUserRepository.findByEmail(creatorEmail)
                                .orElseThrow(() -> new ResourceNotFound("Creator not found"));
                course.setCreator(creator);

                courseRepository.save(course);
                return course;
        }

        public List<CourseResponseDto> getCourses() {
                List<Course> courses = courseRepository.findAll();
                List<CourseResponseDto> coursesResponse = courses.stream()
                                .map(course -> CourseMapper.toResponseDto(course))
                                .toList();

                return coursesResponse;
        }

        public CourseResponseDto getCourse(Long courseId) {
                Course course = courseRepository.findById(courseId)
                                .orElseThrow(() -> new ResourceNotFound("Course not found"));
                CourseResponseDto coursesResponse = CourseMapper.toResponseDto(course);

                return coursesResponse;
        }

        public List<CourseResponseDto> getCreatedByMeCourses(String creatorEmail) {
                List<Course> courses = courseRepository.findAllByCreator_Email(creatorEmail);
                List<CourseResponseDto> coursesResponse = courses.stream()
                                .map(course -> CourseMapper.toResponseDto(course))
                                .toList();

                return coursesResponse;
        }

        public UserCourseEnrollmentResponseDto enroll(String userEmail, Long courseId) {
                AppUser user = appUserRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new ResourceNotFound("User not found"));

                Course course = courseRepository.findById(courseId)
                                .orElseThrow(() -> new ResourceNotFound("Course not found"));

                if (user.getId().equals(course.getCreator().getId())) {
                        throw new ForbiddenActionException("Course creator cannot enroll in their own course.");
                }

                Optional<UserCourseEnrollment> enrollmentFromRepo = enrollmentRepository.findByCourseAndUser(course,
                                user);

                if (enrollmentFromRepo.isPresent()) {
                        throw new ResourceAlreadyExistsException("Enrollment for provided user already exists");
                }

                UserCourseEnrollment enrollment = new UserCourseEnrollment();

                enrollment.setCourse(course);
                enrollment.setUser(user);
                enrollment = enrollmentRepository.save(enrollment);

                return UserCourseEnrollmentMapper.toResponseDto(enrollment);
        }

        public List<CourseResponseDto> getEnrolledCourses(String userEmail) {
                AppUser user = appUserRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new ResourceNotFound("User not found"));

                List<Course> courses = enrollmentRepository.findCoursesByUserId(user.getId());

                List<CourseResponseDto> coursesResponse = courses.stream()
                                .map(course -> CourseMapper.toResponseDto(course))
                                .toList();

                return coursesResponse;
        }

        public UserCourseEnrollmentResponseDto userIsEnrolled(String userEmail, Long courseId) {
                AppUser user = appUserRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new ResourceNotFound("User not found"));

                Optional<UserCourseEnrollment> enrollment = enrollmentRepository.findByCourse_IdAndUser_Id(courseId,
                                user.getId());

                if (enrollment.isPresent()) {
                        return UserCourseEnrollmentMapper.toResponseDto(enrollment.get());
                }

                return UserCourseEnrollmentResponseDto.builder().userId(user.getId()).courseId(courseId)
                                .enrolledAt(null).enrolledStatus("not enrolled").build();

        }

        @Getter
        @AllArgsConstructor
        private class CoursePartProgressInfo {
                private int totalNumberOfBlocks;
                private int finishedNumberOfBlocks;
        }

        public CourseProgressWithAnswersResponseDto getProgressWithAnswers(String userEmail, Long courseId) {
                AppUser user = appUserRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new ResourceNotFound("User not found"));

                Course course = courseRepository.findById(courseId)
                                .orElseThrow(() -> new ResourceNotFound("Course not found"));

                // List<ClozeBlockAnswer> clozeAnswers = course.getClozeBlockAnswers();

                List<ClozeBlockUserAnswer> clozeUserAnswers = clozeBlockUserAnswerRepository
                                .findForUserIdAndCourseId(user.getId(), courseId);

                List<Long> multipleChoiceOptionsUserSelectedIds = multipleChoiceUserSelectedAnswerRepository
                                .findMultipleChoiceOptionIdsForUserIdAndCourseId(user.getId(), courseId);

                // List<Long> multipleChoiceBlockUserAnsweredIds =
                // multipleChoiceUserSelectedAnswerRepository
                // .findMultipleChoiceBlockIdsForUserIdAndCourseId(user.getId(), courseId);

                // List<MultipleChoiceBlock> multipleChoices = course.getMultipleChoiceBlocks();

                // List<MultipleChoiceBlock> userFinishedMultipleChoiceBlock =

                // progress.setCourseId(courseId);
                // progress.setUserId(user.getId());

                // TODO - add other blocks
                return aggregateCourseProgress(course, user.getId(), clozeUserAnswers,
                                multipleChoiceOptionsUserSelectedIds);
        }

        private CourseProgressWithAnswersResponseDto aggregateCourseProgress(Course course, Long userId,
                        List<ClozeBlockUserAnswer> clozeUserAnswers, List<Long> multipleChoiceOptionsUserSelectedIds) {
                CourseProgressWithAnswersResponseDto courseProgress = new CourseProgressWithAnswersResponseDto();
                courseProgress.setCourseId(course.getId());
                courseProgress.setUserId(userId);

                // TODO - add this value
                // CoursePartProgressInfo progressInfo = calculateProgressForCourse(course,
                // clozeUserAnswers);
                // Integer progress = (progressInfo.getFinishedNumberOfBlocks() * 100)
                // / progressInfo.getTotalNumberOfBlocks();
                // courseProgress.setProgress(progress);

                // TODO - delete this dummy value
                courseProgress.setProgress(70);
                for (Unit unit : course.getUnits()) {
                        courseProgress.getUnitProgresses().add(
                                        aggregateUnitProgress(unit, clozeUserAnswers));
                }
                return courseProgress;
        }

        private UnitProgressWithAnswersResponseDto aggregateUnitProgress(Unit unit,
                        List<ClozeBlockUserAnswer> clozeUserAnswers) {
                UnitProgressWithAnswersResponseDto unitProgress = new UnitProgressWithAnswersResponseDto();
                unitProgress.setUnitId(unit.getId());

                // TODO - add this value
                // CoursePartProgressInfo progressInfo = calculateProgressForUnit(unit,
                // clozeUserAnswers);
                // Integer progress = (progressInfo.getFinishedNumberOfBlocks() * 100)
                // / progressInfo.getTotalNumberOfBlocks();
                // unitProgress.setProgress(progress);

                // TODO - delete this dummy value
                unitProgress.setProgress(50);

                for (Lesson lesson : unit.getLessons()) {
                        unitProgress.getLessonProgresses().add(aggregateLessonProgress(lesson, clozeUserAnswers));
                }

                return unitProgress;
        }

        private LessonProgressWithAnswersResponseDto aggregateLessonProgress(Lesson lesson,
                        List<ClozeBlockUserAnswer> clozeUserAnswers) {
                LessonProgressWithAnswersResponseDto lessonProgress = new LessonProgressWithAnswersResponseDto();
                lessonProgress.setLessonId(lesson.getId());

                // TODO - add this value
                // CoursePartProgressInfo progressInfo = calculateProgressForLesson(lesson,
                // clozeUserAnswers);
                // Integer progress = (progressInfo.getFinishedNumberOfBlocks() * 100)
                // / progressInfo.getTotalNumberOfBlocks();

                // lessonProgress.setProgress(progress);

                // TODO - delete this dummy value
                // if (lesson.getId() % 2 == 0) {
                // lessonProgress.setProgress(50);
                // } else {
                lessonProgress.setProgress(100);
                // }
                for (ClozeBlockUserAnswer clozeUserAnswer : clozeUserAnswers) {
                        lessonProgress.getClozeAnswers()
                                        .add(new ClozeBlockUserProgressWithAnswersResponseDto(clozeUserAnswer.getId(),
                                                        clozeUserAnswer.getClozeBlockAnswer().getId(),
                                                        clozeUserAnswer.getUserInput()));
                }
                return lessonProgress;
        }

        private CoursePartProgressInfo calculateProgressForCourse(Course course,
                        List<ClozeBlockUserAnswer> clozeUserAnswers) {
                return course
                                .getUnits()
                                .stream()
                                .map(unit -> calculateProgressForUnit(unit, clozeUserAnswers))
                                .reduce(
                                                new CoursePartProgressInfo(0, 0),
                                                (subProgress, currProgress) -> {
                                                        return new CoursePartProgressInfo(
                                                                        subProgress.getTotalNumberOfBlocks()
                                                                                        + currProgress.getTotalNumberOfBlocks(),
                                                                        subProgress.getFinishedNumberOfBlocks()
                                                                                        + currProgress.getFinishedNumberOfBlocks());
                                                });
        }

        private CoursePartProgressInfo calculateProgressForUnit(Unit unit,
                        List<ClozeBlockUserAnswer> clozeUserAnswers) {
                return unit
                                .getLessons()
                                .stream()
                                .map(lesson -> calculateProgressForLesson(lesson, clozeUserAnswers))
                                .reduce(
                                                new CoursePartProgressInfo(0, 0),
                                                (subProgress, currProgress) -> {
                                                        return new CoursePartProgressInfo(
                                                                        subProgress.getTotalNumberOfBlocks()
                                                                                        + currProgress.getTotalNumberOfBlocks(),
                                                                        subProgress.getFinishedNumberOfBlocks()
                                                                                        + currProgress.getFinishedNumberOfBlocks());
                                                });
        }

        private CoursePartProgressInfo calculateProgressForLesson(Lesson lesson,
                        List<ClozeBlockUserAnswer> clozeUserAnswers) {

                Set<Long> clozeAnswerIdsSet = lesson.getClozeBlockAnswers().stream().map(ClozeBlockAnswer::getId)
                                .collect(Collectors.toSet());

                List<ClozeBlockUserAnswer> clozeUserAnswersForLesson = clozeUserAnswers.stream()
                                .filter(clozeBlockUserAnswer -> clozeAnswerIdsSet
                                                .contains(clozeBlockUserAnswer.getId()))
                                .toList();

                return new CoursePartProgressInfo(clozeAnswerIdsSet.size(),
                                clozeUserAnswersForLesson.size());

        }

}
