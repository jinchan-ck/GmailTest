package com.google.android.common.http;

import java.io.IOException;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;

public class TestHttpClient
{
  private final ConnectionReuseStrategy connStrategy;
  private final HttpContext context;
  private final HttpRequestExecutor httpexecutor;
  private final BasicHttpProcessor httpproc;
  private final HttpParams params = new BasicHttpParams();

  public TestHttpClient()
  {
    this.params.setIntParameter("http.socket.timeout", 5000).setBooleanParameter("http.connection.stalecheck", false).setParameter("http.protocol.version", HttpVersion.HTTP_1_1).setParameter("http.useragent", "TEST-CLIENT/1.1");
    this.httpproc = new BasicHttpProcessor();
    this.httpproc.addInterceptor(new RequestContent());
    this.httpproc.addInterceptor(new RequestTargetHost());
    this.httpproc.addInterceptor(new RequestConnControl());
    this.httpproc.addInterceptor(new RequestUserAgent());
    this.httpproc.addInterceptor(new RequestExpectContinue());
    this.httpexecutor = new HttpRequestExecutor();
    this.connStrategy = new DefaultConnectionReuseStrategy();
    this.context = new BasicHttpContext(null);
  }

  public HttpResponse execute(HttpRequest paramHttpRequest, HttpHost paramHttpHost, HttpClientConnection paramHttpClientConnection)
    throws HttpException, IOException
  {
    this.context.setAttribute("http.request", paramHttpRequest);
    this.context.setAttribute("http.target_host", paramHttpHost);
    this.context.setAttribute("http.connection", paramHttpClientConnection);
    paramHttpRequest.setParams(new DefaultedHttpParams(paramHttpRequest.getParams(), this.params));
    this.httpexecutor.preProcess(paramHttpRequest, this.httpproc, this.context);
    HttpResponse localHttpResponse = this.httpexecutor.execute(paramHttpRequest, paramHttpClientConnection, this.context);
    localHttpResponse.setParams(new DefaultedHttpParams(localHttpResponse.getParams(), this.params));
    this.httpexecutor.postProcess(localHttpResponse, this.httpproc, this.context);
    return localHttpResponse;
  }

  public HttpParams getParams()
  {
    return this.params;
  }

  public boolean keepAlive(HttpResponse paramHttpResponse)
  {
    return this.connStrategy.keepAlive(paramHttpResponse, this.context);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.http.TestHttpClient
 * JD-Core Version:    0.6.2
 */