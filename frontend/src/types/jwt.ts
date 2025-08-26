export interface JwtPayload {
    userId: number;
    userName: string;
    sub: string;    // email
    iat?: number;
    exp?: number;
}