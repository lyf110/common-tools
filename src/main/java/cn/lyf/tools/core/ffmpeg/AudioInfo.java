package cn.lyf.tools.core.ffmpeg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lyf
 * @description
 * @since 2023/5/4 18:44:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioInfo implements Serializable {
    private static final long serialVersionUID = 4996192831460260609L;
    private String decoder;
    private int samplingRate = -1;
    private int channels = -1;
    private int bitRate = -1;
}
