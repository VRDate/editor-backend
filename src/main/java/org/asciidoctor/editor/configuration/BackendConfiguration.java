package org.asciidoctor.editor.configuration;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;

public class BackendConfiguration implements PropertyFileConfig
{
    public static final String KEY_OUTPUT_DIR = "editor.backend.output.dir";

    public static final String KEY_FIREBASE_URL = "editor.backend.firebase.url";

    public static final String KEY_FIREBASE_SECRET = "editor.backend.firebase.secret";

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