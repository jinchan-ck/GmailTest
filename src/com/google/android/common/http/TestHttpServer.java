package com.google.android.common.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.http.ConnectionClosedException;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpException;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpExpectationVerifier;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

public class TestHttpServer
{
  private final ConnectionReuseStrategy connStrategy;
  private HttpExpectationVerifier expectationVerifier;
  private final BasicHttpProcessor httpproc;
  private Thread listener;
  private final HttpParams params = new BasicHttpParams();
  private final HttpRequestHandlerRegistry reqistry;
  private final HttpResponseFactory responseFactory;
  private final ServerSocket serversocket;
  private volatile boolean shutdown;

  public TestHttpServer()
    throws IOException
  {
    this(0);
  }

  public TestHttpServer(int paramInt)
    throws IOException
  {
    this.params.setIntParameter("http.socket.timeout", 20000).setIntParameter("http.socket.buffer-size", 8192).setBooleanParameter("http.connection.stalecheck", false).setBooleanParameter("http.tcp.nodelay", true).setParameter("http.origin-server", "TEST-SERVER/1.1");
    this.httpproc = new BasicHttpProcessor();
    this.httpproc.addInterceptor(new ResponseDate());
    this.httpproc.addInterceptor(new ResponseServer());
    this.httpproc.addInterceptor(new ResponseContent());
    this.httpproc.addInterceptor(new ResponseConnControl());
    this.connStrategy = new DefaultConnectionReuseStrategy();
    this.responseFactory = new DefaultHttpResponseFactory();
    this.reqistry = new HttpRequestHandlerRegistry();
    this.serversocket = new ServerSocket(paramInt);
  }

  private HttpServerConnection acceptConnection()
    throws IOException
  {
    Socket localSocket = this.serversocket.accept();
    DefaultHttpServerConnection localDefaultHttpServerConnection = new DefaultHttpServerConnection();
    localDefaultHttpServerConnection.bind(localSocket, this.params);
    return localDefaultHttpServerConnection;
  }

  public InetAddress getInetAddress()
  {
    return this.serversocket.getInetAddress();
  }

  public int getPort()
  {
    return this.serversocket.getLocalPort();
  }

  public void registerHandler(String paramString, HttpRequestHandler paramHttpRequestHandler)
  {
    this.reqistry.register(paramString, paramHttpRequestHandler);
  }

  public void setExpectationVerifier(HttpExpectationVerifier paramHttpExpectationVerifier)
  {
    this.expectationVerifier = paramHttpExpectationVerifier;
  }

  public void shutdown()
  {
    if (this.shutdown)
      return;
    this.shutdown = true;
    try
    {
      this.serversocket.close();
      label20: this.listener.interrupt();
      try
      {
        this.listener.join(1000L);
        return;
      }
      catch (InterruptedException localInterruptedException)
      {
      }
    }
    catch (IOException localIOException)
    {
      break label20;
    }
  }

  public void start()
  {
    if (this.listener != null)
      throw new IllegalStateException("Listener already running");
    this.listener = new Thread(new Runnable()
    {
      public void run()
      {
        while (true)
        {
          if ((!TestHttpServer.this.shutdown) && (!Thread.interrupted()));
          try
          {
            HttpServerConnection localHttpServerConnection = TestHttpServer.this.acceptConnection();
            HttpService localHttpService = new HttpService(TestHttpServer.this.httpproc, TestHttpServer.this.connStrategy, TestHttpServer.this.responseFactory);
            localHttpService.setParams(TestHttpServer.this.params);
            localHttpService.setExpectationVerifier(TestHttpServer.this.expectationVerifier);
            localHttpService.setHandlerResolver(TestHttpServer.this.reqistry);
            TestHttpServer.WorkerThread localWorkerThread = new TestHttpServer.WorkerThread(localHttpService, localHttpServerConnection);
            localWorkerThread.setDaemon(true);
            localWorkerThread.start();
          }
          catch (InterruptedIOException localInterruptedIOException)
          {
          }
          catch (IOException localIOException)
          {
          }
        }
      }
    });
    this.listener.start();
  }

  static class WorkerThread extends Thread
  {
    private final HttpServerConnection conn;
    private final HttpService httpservice;

    public WorkerThread(HttpService paramHttpService, HttpServerConnection paramHttpServerConnection)
    {
      this.httpservice = paramHttpService;
      this.conn = paramHttpServerConnection;
    }

    public void run()
    {
      BasicHttpContext localBasicHttpContext = new BasicHttpContext(null);
      try
      {
        while ((!Thread.interrupted()) && (this.conn.isOpen()))
          this.httpservice.handleRequest(this.conn, localBasicHttpContext);
      }
      catch (ConnectionClosedException localConnectionClosedException)
      {
        localConnectionClosedException = localConnectionClosedException;
      }
      catch (IOException localIOException3)
      {
        try
        {
          this.conn.shutdown();
          return;
          localIOException3 = localIOException3;
          System.err.println("I/O error: " + localIOException3.getMessage());
          this.conn.shutdown();
          return;
        }
        catch (IOException localIOException2)
        {
          return;
        }
      }
      catch (HttpException localHttpException)
      {
        localHttpException = localHttpException;
        System.err.println("Unrecoverable HTTP protocol violation: " + localHttpException.getMessage());
        this.conn.shutdown();
        return;
      }
      finally
      {
      }
      try
      {
        this.conn.shutdown();
        label149: throw localObject;
        this.conn.shutdown();
        return;
      }
      catch (IOException localIOException1)
      {
        break label149;
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.http.TestHttpServer
 * JD-Core Version:    0.6.2
 */