package com.km.adm.util;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class RestUtil {

	/**
	 * GET 요청
	 */
	public static String getApi(MediaType contentType, String url) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.getMessageConverters()
				.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			HttpEntity<String> entity = new HttpEntity<>(headers);
			return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * GET 요청
	 */
	public static String getApi(MediaType contentType, String url, Map<String, String> params) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.getMessageConverters()
				.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			for (Map.Entry<String, String> entry : params.entrySet()) {
				builder.queryParam(entry.getKey(), entry.getValue());
			}
			HttpEntity<String> entity = new HttpEntity<>(headers);
			return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				String.class).getBody();
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * GET 요청
	 */
	public static String getApiWithHeaders(MediaType contentType, String url,
		Map<String, String> headerMap) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.getMessageConverters()
				.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			for (String key : headerMap.keySet()) {
				headers.add(key, headerMap.get(key));
			}
			HttpEntity<String> entity = new HttpEntity<>(headers);
			return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * GET 요청
	 */
	public static String getApiWithHeadersAndQueryParams(MediaType contentType, String url,
		Map<String, String> headerMap, Map<String, String> params) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			for (String key : headerMap.keySet()) {
				headers.add(key, headerMap.get(key));
			}
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			for (Map.Entry<String, String> entry : params.entrySet()) {
				builder.queryParam(entry.getKey(), entry.getValue());
			}
			HttpEntity<String> entity = new HttpEntity<>(headers);
			return restTemplate.exchange(builder.build(false).toUriString(), HttpMethod.GET, entity,
				String.class).getBody();
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * POST 요청
	 */
	public static String postApi(MediaType contentType, String url, String json) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.getMessageConverters()
				.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			HttpEntity<String> entity = new HttpEntity<>(json, headers);
			return restTemplate.postForObject(url, entity, String.class);
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error("[JSON] " + json);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * POST 요청
	 */
	public static String postApiWithHeaders(MediaType contentType, String url, String json, Map<String, String> headerMap) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.getMessageConverters()
				.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			for (String key : headerMap.keySet()) {
				headers.add(key, headerMap.get(key));
			}
			HttpEntity<String> entity = new HttpEntity<>(json, headers);
			return restTemplate.postForObject(url, entity, String.class);
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error("[JSON] " + json);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * POST 요청
	 */
	public static String postApiWithQueryParams(MediaType contentType, String url, String json, Map<String, Object> params) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				builder.queryParam(entry.getKey(), entry.getValue());
			}
			HttpEntity<String> entity = new HttpEntity<>(json, headers);
			return restTemplate.postForObject(builder.toUriString(), entity, String.class);
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error("[JSON] " + json);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * POST 요청
	 */
	public static String postApiWithHeadersAndQueryParams(String url,
		Map<String, String> headerMap, Map<String, Object> params) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			HttpHeaders headers = new HttpHeaders();
			for (String key : headerMap.keySet()) {
				headers.add(key, headerMap.get(key));
			}
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				builder.queryParam(entry.getKey(), entry.getValue());
			}
			HttpEntity<String> entity = new HttpEntity<>(headers);
			URI uri = encodeUri(builder.toUriString());
			System.out.println("url은 " + uri);
			return restTemplate.postForObject(uri, entity, String.class);
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error(e.getMessage(), e);
			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * POST 요청
	 */
	public static String postApiWithQueryParams(MediaType contentType, String url, String json,
		Map<String, String> headerMap, Map<String, Object> params) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			for (String key : headerMap.keySet()) {
				headers.add(key, headerMap.get(key));
			}
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				builder.queryParam(entry.getKey(), entry.getValue());
			}
			HttpEntity<String> entity = new HttpEntity<>(json, headers);
			return restTemplate.postForObject(builder.toUriString(), entity, String.class);
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error("[JSON] " + json);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * UriComponentBuilder는 공백과 "+"의 경우 인코딩을 해주지 않아서 직접 인코딩.
	 */
	public static URI encodeUri(String uri) {
		String encodeUri = uri.contains("+") ? uri.replace("+",  "%2B") : uri;
		return UriComponentsBuilder.fromUriString(encodeUri).build(true).toUri();
	}

	/**
	 * POST 요청
	 */
	public static String postApi(MediaType contentType, String url, String json,
		Map<String, String> headerMap) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.getMessageConverters()
				.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			for (String key : headerMap.keySet()) {
				headers.add(key, headerMap.get(key));
			}
			HttpEntity<String> entity = new HttpEntity<>(json, headers);
			return restTemplate.postForObject(url, entity, String.class);
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error("[JSON] " + json);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * PATCH 요청
	 */
	public static String patchApi(MediaType contentType, String url, String json) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

			// 설정 (타임아웃 등)
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분

			RestTemplate restTemplate = new RestTemplate(factory);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			HttpEntity<String> entity = new HttpEntity<>(json, headers);
			return restTemplate.patchForObject(url, entity, String.class);
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error("[JSON] " + json);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * PATCH 요청
	 */
	public static String patchApi(MediaType contentType, String url, String json,
		Map<String, String> headerMap) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

			// 설정 (타임아웃 등)
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분

			RestTemplate restTemplate = new RestTemplate(factory);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			for (String key : headerMap.keySet()) {
				headers.add(key, headerMap.get(key));
			}
			HttpEntity<String> entity = new HttpEntity<>(json, headers);
			return restTemplate.patchForObject(url, entity, String.class);
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error("[JSON] " + json);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * PUT 요청
	 */
	public static String putApi(MediaType contentType, String url, String json) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			HttpEntity<String> entity = new HttpEntity<>(json, headers);
			return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error("[JSON] " + json);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * PUT 요청
	 */
	public static String putApi(MediaType contentType, String url, String json,
		Map<String, String> headerMap) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			for (String key : headerMap.keySet()) {
				headers.add(key, headerMap.get(key));
			}
			HttpEntity<String> entity = new HttpEntity<>(json, headers);
			return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error("[JSON] " + json);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * DELETE 요청
	 */
	public static String deleteApi(MediaType contentType, String url, String json) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.getMessageConverters()
				.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			HttpEntity<String> entity = new HttpEntity<>(json, headers);
			return restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class).getBody();
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error("[JSON] " + json);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	/**
	 * DELETE 요청
	 */
	public static String deleteApi(MediaType contentType, String url, String json,
		Map<String, String> headerMap) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);
			restTemplate.getMessageConverters()
				.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			for (String key : headerMap.keySet()) {
				headers.add(key, headerMap.get(key));
			}
			HttpEntity<String> entity = new HttpEntity<>(json, headers);
			return restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class).getBody();
		} catch (Exception e) {
			log.error("[URL] " + url);
			log.error("[JSON] " + json);
			log.error(e.getMessage(), e);

			return AppConstants.MODE_ERROR;
		}
	}

	public static String postApiWithQueryParams(MediaType contentType, String url, Map<String, String> headerMap, Map<String, Object> params) {
		try {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 5초
			factory.setReadTimeout(180000); // 3분
			RestTemplate restTemplate = new RestTemplate(factory);

			// Set headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(contentType);
			for (String key : headerMap.keySet()) {
				headers.add(key, headerMap.get(key));
			}

			// Set form parameters
			MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				formParams.add(entry.getKey(), entry.getValue().toString());
			}

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formParams, headers);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

			return restTemplate.postForEntity(builder.toUriString(), entity, String.class).getBody();
		} catch (Exception e) {
			System.err.println("[URL] " + url);
			e.printStackTrace();
			return AppConstants.MODE_ERROR;
		}
	}

	
}
