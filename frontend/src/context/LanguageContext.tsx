import { createContext, useState, type ReactNode } from "react";
import type { AVAILABLE_LANGUAGES_TYPE } from "../constants";


interface LanguageContextType {
    language: AVAILABLE_LANGUAGES_TYPE
    setLanguage: (language: AVAILABLE_LANGUAGES_TYPE) => void;
}

export const LanguageContext = createContext<LanguageContextType | undefined>(undefined);

export const LanguageProvider = ({ children }: { children: ReactNode }) => {
    const [selectedLanguage, setSelectedLanguage] = useState<AVAILABLE_LANGUAGES_TYPE>("English");
    return (
        <LanguageContext.Provider value={{ language: selectedLanguage, setLanguage: setSelectedLanguage }}>
            {children}
        </LanguageContext.Provider>
    );
}