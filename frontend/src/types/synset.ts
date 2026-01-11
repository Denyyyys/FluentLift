export type LanguageData = {
    language: string;
    lemma?: string | null;
    synonyms?: string[] | null;
    definition?: string | null;
    examples?: string[] | null;
    id: string;
};

export interface Synset {
    synset_id: string;
    pos: string;
    languages: LanguageData[];
}

