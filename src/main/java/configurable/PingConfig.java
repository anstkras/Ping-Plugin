package configurable;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(
        name = "PingConfig",
        storages = {
                @Storage("PingConfig.xml")}
)
public class PingConfig implements PersistentStateComponent<PingConfig> {

    @Nullable
    public static PingConfig getInstance(Project project) {
        return ServiceManager.getService(project, PingConfig.class);
    }

    @Nullable
    @Override
    public PingConfig getState() {
        return this;
    }

    @Override
    public void loadState(PingConfig singleFileExecutionConfig) {
        XmlSerializerUtil.copyBean(singleFileExecutionConfig, this);
    }
}
