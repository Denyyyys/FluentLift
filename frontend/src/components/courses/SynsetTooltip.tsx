import type { Synset } from "../../types/synset";
import { getLanguageCode } from "../../utils/utils";

interface SynsetTooltipProps {
    synset: Synset | null;
    sourceLang: string;
    targetLang: string;
    selectedText: string;
    selectedTextPosition: { x: number; y: number },

}

function SynsetTooltip({
    synset,
    sourceLang,
    targetLang,
    selectedText,
    selectedTextPosition
}: SynsetTooltipProps) {
    if (!synset) {
        return (
            <div className="p-2 bg-gray-100 border rounded shadow-md">
                <strong>{selectedText}</strong>: No synset found
            </div>
        );
    }

    const getLangData = (languageName: string) =>
        synset.languages.find((l) => l.language === getLanguageCode(languageName));

    const posToSourceLang = (pos: String) => {
        if (targetLang == "Polish") {
            switch (pos) {
                case "noun":
                    return "rzeczownik"
                case "verb":
                    return "czasownik"
                case "adjective":
                    return "przymiotnik"
                case "adverb":
                    return "przysłówek"
                default:
                    break;
            }
        }
    }

    console.log(posToSourceLang(synset.pos));


    const renderLanguageBlock = (langCode: string) => {
        const data = getLangData(langCode);
        if (!data) {
            return (
                <div className="mb-2">
                    <strong>{langCode}:</strong> Not found
                </div>
            );
        }

        return (
            <div className="mb-4">
                <h4 className="font-semibold">{langCode}</h4>
                {data.lemma && <div><strong>Lemma:</strong> {data.lemma}</div>}
                {data.definition && <div><strong>Definition:</strong> {data.definition}</div>}
                {data.synonyms && data.synonyms.length > 0 && (
                    <div>
                        <strong>Synonyms:</strong> {data.synonyms.join(", ")}
                    </div>
                )}
                {data.examples && data.examples.length > 0 && (
                    <div>
                        <strong>Examples:</strong>
                        <ul className="list-disc ml-5">
                            {data.examples.map((ex, idx) => (
                                <li key={idx}>{ex}</li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>
        );
    };

    return (
        <div className="p-4 bg-white border rounded shadow-md max-w-md"
            style={{
                position: "absolute",
                top: selectedTextPosition.y,
                left: selectedTextPosition.x,
                backgroundColor: "var(--clr-primary-300)",
                border: "1px solid black",
                padding: "5px 10px",
                borderRadius: "4px",
                boxShadow: "0 2px 8px rgba(0,0,0,0.2)",
                zIndex: 1000,
            }}
        >
            <div className="mb-2">
                <strong>Selected:</strong> {selectedText}
            </div>
            <div>
                {renderLanguageBlock(sourceLang)}
                {renderLanguageBlock(targetLang)}
            </div>
            <div className="text-sm text-gray-500">POS: {synset.pos} {posToSourceLang(synset.pos) && `(${posToSourceLang(synset.pos)})`}</div>
        </div>
    );
};

export default SynsetTooltip;
