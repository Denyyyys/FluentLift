import { textByLanguage } from "../../assets/translations"
import { useLanguage } from "../../hooks/useLanguage"

function RouteNotFound() {
    const { language } = useLanguage()
    return (
        <h1 className="mt-4">{textByLanguage[language]["errorMessages"]["pageDoesNotExistMessageText"]}</h1>
    )
}

export default RouteNotFound