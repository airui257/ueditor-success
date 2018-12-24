package com.baidu.ueditor.upload;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BinaryUploader {

	public static final State save(HttpServletRequest request, Map<String, Object> conf) {

		boolean isAjaxUpload = request.getHeader("X_Requested_With") != null;

		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}
		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
		if (isAjaxUpload) {
			upload.setHeaderEncoding("UTF-8");
		}
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile(conf.get("fieldName").toString());
			if (multipartFile == null) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}
			String originFileName = multipartFile.getOriginalFilename();
			String suffix = FileType.getSuffixByFilename(originFileName);
			long maxSize = ((Long) conf.get("maxSize")).longValue();
			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}
			RestTemplate restTemplate = new RestTemplate();
			MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();

			File temp = File.createTempFile("temp", suffix);
			multipartFile.transferTo(temp);

			FileSystemResource resource = new FileSystemResource(temp.getAbsoluteFile());
			param.add("file", resource);
			String result = restTemplate.postForObject("http://127.0.0.1:7090/fdfs/upload", param, String.class);
			String[] split = result.split("/");
			State storageState = new BaseState();
			storageState.putInfo("url", "http://10.160.2.43:8888/"+PathFormat.format(result));
//			storageState.putInfo("url", PathFormat.format(result));
			storageState.putInfo("type", suffix);
			storageState.putInfo("original", originFileName);
			storageState.putInfo("size", maxSize);
			storageState.putInfo("state", "SUCCESS");
			storageState.putInfo("title", split[split.length-1]);
			System.out.println("storageState = " + storageState);
			return storageState;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
