package kr.ac.koreatech.os.pss.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 리소스 로딩에 필요한 유틸리티 클래스
 *
 * @author refracta
 */
public class ResourceUtils {
    /**
     * 주어진 경로 폴더에 있는 리소스 요소의 이름을 리스트로 가져온다.
     *
     * @param path 리소스 경로 폴더
     * @return 리소스 요소 리스트
     * @throws IOException
     */
    public static List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();

        try (InputStream in = getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }

        return filenames;
    }

    /**
     * 주어진 경로의 리소스를 스트림으로 가져온다.
     *
     * @param resource 리소스 경로
     * @return 리소스의 InputStream
     */
    public static InputStream getResourceAsStream(String resource) {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        return in == null ? ResourceUtils.class.getResourceAsStream(resource) : in;
    }

    /**
     * 주어진 클래스의 패키지 경로를 반환한다.
     *
     * @param target
     * @return 클래스의 패키지 경로 ('/'로 구분됨)
     */
    public static String getPackagePath(Class target) {
        return target.getPackage().getName().replaceAll("\\.", "/") + "/";
    }
}
