package cn.lyf.tools.system;

import cn.lyf.tools.collection.CollectionUtil;
import cn.lyf.tools.core.constant.CharsetEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author lyf
 * @description 调用系统操作工具cmd的工具类
 * @since 2023/5/4 15:18:46
 */
@Slf4j
public final class CmdUtil {
    private CmdUtil() {
    }

    /**
     * 运行cmd命令
     *
     * @param command cmd命令 ex: cmd /c dir
     * @return 运行结果
     * @throws IOException IOException
     */
    public static String runCmd(String command) throws IOException {
        return runCmd(command, CharsetEnum.UTF_8.getCharsetName());
    }

    /**
     * 运行cmd命令
     *
     * @param command     cmd命令 ex: cmd /c dir
     * @param charsetName 结果的字符编码
     * @return 运行结果
     * @throws IOException IOException
     */
    public static String runCmd(String command, String charsetName) throws IOException {
        // 执行命令
        Process process = Runtime.getRuntime().exec(command);
        return handlerProcessExecuteInfo(process, charsetName);
    }

    /**
     * 运行cmd命令
     *
     * @param command cmd命令
     * @param envp    如果envp为null那么子进程会继承当前进程的环境设置。
     * @param dir     当前的文件目录
     * @return 运行结果
     * @throws IOException IOException
     */
    public static String runCmd(String command, String[] envp, File dir) throws IOException {
        return runCmd(command, envp, dir, CharsetEnum.UTF_8.getCharsetName());
    }

    /**
     * 运行cmd命令
     *
     * @param command     cmd命令
     * @param envp        如果envp为null那么子进程会继承当前进程的环境设置。
     * @param dir         当前的文件目录
     * @param charsetName 结果的字符编码
     * @return 运行结果
     * @throws IOException IOException
     */
    public static String runCmd(String command, String[] envp, File dir, String charsetName) throws IOException {
        // 执行命令
        Process process = Runtime.getRuntime().exec(command, envp, dir);
        return handlerProcessExecuteInfo(process, charsetName);
    }

    /**
     * 执行命令集
     *
     * @param commandList 命令集
     * @return 结果
     */
    public static String invokeCommand(List<String> commandList) {
        return invokeCommand(commandList, CharsetEnum.UTF_8.getCharsetName());
    }

    /**
     * 执行命令集
     *
     * @param commandList 命令集
     * @param charsetName 结果的字符编码集
     * @return 结果
     */
    public static String invokeCommand(List<String> commandList, String charsetName) {
        if (CollectionUtil.isEmpty(commandList)) {
            log.error("commandList is null");
            return null;
        }
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commandList);

        // 正常信息和错误信息合并输出
        builder.redirectErrorStream(true);

        String result = null;
        try {
            // 开始执行命令
            Process process = builder.start();
            result = handlerProcessExecuteInfo(process, charsetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 保存进程执行信息
     *
     * @param process 进程
     * @throws IOException io异常
     */
    private static String handlerProcessExecuteInfo(Process process) throws IOException {
        return handlerProcessExecuteInfo(process, CharsetEnum.UTF_8.getCharsetName());
    }

    /**
     * 保存进程执行信息
     *
     * @param process 进程
     * @throws IOException io异常
     */
    private static String handlerProcessExecuteInfo(Process process, String charsetName) throws IOException {
        // 获取执行后的结果信息
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(),
                charsetName))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }
}
