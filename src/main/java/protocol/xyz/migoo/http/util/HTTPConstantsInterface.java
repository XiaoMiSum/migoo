/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package protocol.xyz.migoo.http.util;

public interface HTTPConstantsInterface {
    String HEADERS = "headers";
    String COOKIE = "cookie";
    String COOKIE_NAME = "name";
    String COOKIE_VALUE = "value";
    String COOKIE_DOMAIN = "domain";
    String COOKIE_PATH = "path";

    String HTTP_DEFAULT = "migoo.protocol.http.element.defaults";
    String HOST = "host";
    String PORT = "port";
    String PROTOCOL = "protocol";
    String BASE_PATH = "base_path";
    String API_PATH = "api";
    String REQUEST_METHOD = "method";
    String BODY = "body";
    String DATA = "data";
    String QUERY = "query";

    String SEPARATOR = "/";

    String SC_MOVED_PERMANENTLY = "301";
    String SC_MOVED_TEMPORARILY = "302";
    String SC_SEE_OTHER = "303";
    String SC_TEMPORARY_REDIRECT = "307";

    int DEFAULT_HTTPS_PORT = 443;
    String DEFAULT_HTTPS_PORT_STRING = "443";
    int DEFAULT_HTTP_PORT = 80;
    String DEFAULT_HTTP_PORT_STRING = "80";
    String PROTOCOL_HTTP = "http";
    String PROTOCOL_HTTPS = "https";
    String HEAD = "HEAD";
    String POST = "POST";
    String PUT = "PUT";
    String GET = "GET";
    String OPTIONS = "OPTIONS";
    String TRACE = "TRACE";
    String DELETE = "DELETE";
    String PATCH = "PATCH";
    String PROPFIND = "PROPFIND";
    String PROPPATCH = "PROPPATCH";
    String MKCOL = "MKCOL";
    String COPY = "COPY";
    String MOVE = "MOVE";
    String LOCK = "LOCK";
    String UNLOCK = "UNLOCK";
    String CONNECT = "CONNECT";
    String REPORT = "REPORT";
    String MKCALENDAR = "MKCALENDAR";
    String SEARCH = "SEARCH";
    String HEADER_AUTHORIZATION = "Authorization";
    String HEADER_COOKIE = "Cookie";
    String HEADER_COOKIE_IN_REQUEST = "Cookie:";
    String HEADER_CONNECTION = "Connection";
    String CONNECTION_CLOSE = "close";
    String KEEP_ALIVE = "keep-alive";
    String TRANSFER_ENCODING = "transfer-encoding";
    String HEADER_CONTENT_ENCODING = "content-encoding";
    String HTTP_1_1 = "HTTP/1.1";
    String HEADER_SET_COOKIE = "set-cookie";
    String ENCODING_BROTLI = "br";
    String ENCODING_DEFLATE = "deflate";
    String ENCODING_GZIP = "gzip";

    String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    String HEADER_CONTENT_TYPE = "Content-Type";
    String HEADER_CONTENT_LENGTH = "Content-Length";
    String HEADER_HOST = "Host";
    String HEADER_LOCAL_ADDRESS = "X-LocalAddress";
    String HEADER_LOCATION = "Location";
    String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    String MULTIPART_FORM_DATA = "multipart/form-data";
    // For handling caching
    String IF_NONE_MATCH = "If-None-Match";
    String IF_MODIFIED_SINCE = "If-Modified-Since";
    String ETAG = "Etag";
    String LAST_MODIFIED = "Last-Modified";
    String EXPIRES = "Expires";
    String CACHE_CONTROL = "Cache-Control";
    String DATE = "Date";
    String VARY = "Vary";

}