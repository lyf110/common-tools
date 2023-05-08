package cn.lyf.tools.core.ffmpeg;

import it.sauronsoftware.jave.VideoSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lyf
 * @description
 * @since 2023/5/4 18:46:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoInfo implements Serializable {
    private static final long serialVersionUID = -8989285736315290024L;
    private String decoder;
    private VideoSize size = null;
    private int bitRate = -1;
    private float frameRate = -1.0F;
}
