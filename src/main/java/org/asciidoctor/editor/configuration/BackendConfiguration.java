package org.asciidoctor.editor.configuration;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;

public class BackendConfiguration implements PropertyFileConfig
{
    public static final String KEY_OUTPUT_DIR = "editor.backend.output.dir";

    public static final String KEY_FIREBASE_URL = "editor.backend.firebase.url";

    public static final String KEY_FIREBASE_SECRET = "editor.backend.firebase.secret";

    public static final String ENV_ASCIIDOCTOR_BACKENDS = "ASCIIDOCTOR_BACKENDS";

    public static final String KEY_BACKENDS_DZSLIDES = "editor.backend.backends.dzslides";

    public static final String KEY_BACKENDS_REVEALJS = "editor.backend.backends.revealjs";

    public static final String KEY_BACKENDS_DECKJS = "editor.backend.backends.deckjs";


    @Override
    public String getPropertyFileName()
    {
        return "editor-backend.properties";
    }

    @Override
    public boolean isOptional() {
        return false;
    }
}