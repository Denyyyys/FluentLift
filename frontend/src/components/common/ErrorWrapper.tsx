
type ErrorWrapperProps = {
    content?: string
}

function ErrorWrapper({ content }: ErrorWrapperProps) {
    return (
        <div className="mt-2">
            <h1>Woah, it seems like there is some kind of error.</h1>
            <h2>{content ? content : "Oops something went wrong. Please see console logs."}</h2>
        </div>
    )
}

export default ErrorWrapper