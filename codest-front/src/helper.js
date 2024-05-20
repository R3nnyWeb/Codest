import {java} from "@codemirror/lang-java";
import {python} from "@codemirror/lang-python";

export default {

    getLanguageExtension(language) {
        switch (language) {
            case "java":
                return [java()]
            case "python":
                return [python()]
            default:
                return []
        }
    },

    isDarkTheme() {
       return  (window.matchMedia('(prefers-color-scheme: dark)').matches)
    }

}