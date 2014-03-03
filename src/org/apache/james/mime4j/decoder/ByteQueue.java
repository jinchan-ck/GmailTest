package org.apache.james.mime4j.decoder;

public class ByteQueue
{
  private UnboundedFifoByteBuffer buf = new UnboundedFifoByteBuffer();
  private int initialCapacity = -1;

  public void clear()
  {
    if (this.initialCapacity != -1)
    {
      this.buf = new UnboundedFifoByteBuffer(this.initialCapacity);
      return;
    }
    this.buf = new UnboundedFifoByteBuffer();
  }

  public int count()
  {
    return this.buf.size();
  }

  public byte dequeue()
  {
    return this.buf.remove();
  }

  public void enqueue(byte paramByte)
  {
    this.buf.add(paramByte);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     org.apache.james.mime4j.decoder.ByteQueue
 * JD-Core Version:    0.6.2
 */