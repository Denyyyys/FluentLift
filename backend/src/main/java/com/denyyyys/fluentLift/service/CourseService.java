package com.denyyyys.fluentLift.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.denyyyys.fluentLift.exceptions.ForbiddenActionException;
import com.denyyyys.fluentLift.exceptions.ResourceAlreadyExistsException;
import com.denyyyys.fluentLift.exceptions.ResourceNotFound;
import com.denyyyys.fluentLift.model.postgres.dto.request.UserAnswersRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.course.CourseCreateDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CourseAnswersResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.CourseResponseDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.UserCourseEnrollmentResponseDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.entity.course.Course;
import com.denyyyys.fluentLift.model.postgres.entity.course.UserCourseEnrollment;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.ClozeBlockUserAnswer;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.MultipleChoiceOption;
import com.denyyyys.fluentLift.model.postgres.entity.course.lessonBlock.MultipleChoiceUserSelectedAnswer;
import com.denyyyys.fluentLift.model.postgres.mapper.CourseMapper;
import com.denyyyys.fluentLift.model.postgres.mapper.UserCourseEnrollmentMapper;
import com.denyyyys.fluentLift.repo.postgres.AppUserRepository;
import com.denyyyys.fluentLift.repo.postgres.ClozeBlockUserAnswerRepository;
import com.denyyyys.fluentLift.repo.postgres.CourseRepository;
import com.denyyyys.fluentLift.repo.postgres.MultipleChoiceOptionRepository;
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
        // private final MultipleChoiceBlockRepository multipleChoiceBlockRepository;
        private final MultipleChoiceUserSelectedAnswerRepository multipleChoiceUserSelectedAnswerRepository;
        private final MultipleChoiceOptionRepository multipleChoiceOptionRepository;
        private final AppUserRepository appUserRepository;

        @Transactional(transactionManager = "transactionManager")
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

        public CourseAnswersResponseDto getProgressWithAnswers(String userEmail, Long courseId) {
                AppUser user = appUserRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new ResourceNotFound("User not found"));

                Course course = courseRepository.findById(courseId)
                                .orElseThrow(() -> new ResourceNotFound("Course not found"));

                List<ClozeBlockUserAnswer> clozeUserAnswers = clozeBlockUserAnswerRepository
                                .findAllByUserIdAndCourseId(user.getId(), courseId);

                Map<Long, List<ClozeBlockUserAnswer>> userClozeAnswersByLessonId = clozeUserAnswers.stream()
                                .collect(Collectors.groupingBy(
                                                answer -> answer.getClozeBlockAnswer().getCloze().getLesson().getId()));

                List<MultipleChoiceUserSelectedAnswer> userSelectedMultipleChoiceOptions = multipleChoiceUserSelectedAnswerRepository
                                .findMultipleChoiceOptionIdsForUserIdAndCourseId(user.getId(), courseId);

                Map<Long, List<Long>> userSelectedMcosIdsByLessonId = userSelectedMultipleChoiceOptions
                                .stream()
                                .collect(Collectors.groupingBy(
                                                mco -> mco.getMultipleChoiceOption().getMultipleChoiceBlock()
                                                                .getLesson().getId(),
                                                Collectors.mapping(
                                                                mco -> mco.getMultipleChoiceOption().getId(),
                                                                Collectors.toList())));

                CourseAnswersResponseDto response = CourseMapper.toCourseAnswersResponseDto(course, user,
                                userClozeAnswersByLessonId,
                                userSelectedMcosIdsByLessonId);

                return response;
        }

        @Transactional(transactionManager = "transactionManager")
        public String saveUserAnswers(String userEmail, UserAnswersRequestDto userAnswers, Long lessonId) {
                AppUser user = appUserRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new ResourceNotFound("User not found"));

                List<ClozeBlockUserAnswer> clozeUserAnswers = CourseMapper
                                .toClozeAnswersEntity(userAnswers.getClozeAnswers(), user);

                List<MultipleChoiceOption> choiceOptions = multipleChoiceOptionRepository
                                .findAllByIdIn(userAnswers.getMultipleChoiceUserSelectedOptionIds());
                List<MultipleChoiceUserSelectedAnswer> mcUserAnswers = CourseMapper.toMultipleChoiceAnswersEntity(
                                choiceOptions, user);

                clozeBlockUserAnswerRepository.deleteAllByUserAndClozeBlockAnswer_Cloze_LessonId(user, lessonId);
                clozeBlockUserAnswerRepository.saveAll(clozeUserAnswers);

                multipleChoiceUserSelectedAnswerRepository.deleteAllByUserIdAndLessonId(user.getId(), lessonId);
                multipleChoiceUserSelectedAnswerRepository.saveAll(mcUserAnswers);

                return "saved";
        }

}
