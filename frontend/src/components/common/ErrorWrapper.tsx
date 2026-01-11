import { textByLanguage } from "../../assets/translations"
import { useLanguage } from "../../hooks/useLanguage"

type ErrorWrapperProps = {
    content?: string
}

function ErrorWrapper({ content }: ErrorWrapperProps) {
    const { language } = useLanguage()
    return (
        <div className="mt-2">
            <h1>{textByLanguage[language]["errorMessages"]["someKindOfErrorOccuredText"]}</h1>
            <h2>{content ? content : textByLanguage[language]["errorMessages"]["somethingWentWrongText"]}</h2>
        </div>
    )
}

export default ErrorWrapper