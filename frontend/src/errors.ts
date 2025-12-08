
export class ErrorBase<T extends string> extends Error {
    name: T;
    message: string;
    cause?: any;

    constructor({ name, message, cause }: { name: T, message: string, cause?: any }) {
        super(message);
        this.name = name;
        this.message = message;
        this.cause = cause
    }
}

export enum NotFoundName {
    CourseNotFound = 'COURSE_NOT_FOUND',
    UnitNotFound = 'UNIT_NOT_FOUND',
    LessonNotFound = 'LESSON_NOT_FOUND',
    GeneralNotFound = 'GENERAL_NOT_FOUND'
}


export class NotFoundError extends ErrorBase<NotFoundName> { }