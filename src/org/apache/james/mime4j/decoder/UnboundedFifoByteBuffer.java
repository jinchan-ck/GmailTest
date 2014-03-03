package org.apache.james.mime4j.decoder;

class UnboundedFifoByteBuffer
{
  protected byte[] buffer;
  protected int head;
  protected int tail;

  public UnboundedFifoByteBuffer()
  {
    this(32);
  }

  public UnboundedFifoByteBuffer(int paramInt)
  {
    if (paramInt <= 0)
      throw new IllegalArgumentException("The size must be greater than 0");
    this.buffer = new byte[paramInt + 1];
    this.head = 0;
    this.tail = 0;
  }

  public boolean add(byte paramByte)
  {
    if (1 + size() >= this.buffer.length)
    {
      byte[] arrayOfByte = new byte[1 + 2 * (-1 + this.buffer.length)];
      int i = 0;
      int j = this.head;
      while (j != this.tail)
      {
        arrayOfByte[i] = this.buffer[j];
        this.buffer[j] = 0;
        i++;
        j++;
        if (j == this.buffer.length)
          j = 0;
      }
      this.buffer = arrayOfByte;
      this.head = 0;
      this.tail = i;
    }
    this.buffer[this.tail] = paramByte;
    this.tail = (1 + this.tail);
    if (this.tail >= this.buffer.length)
      this.tail = 0;
    return true;
  }

  public boolean isEmpty()
  {
    return size() == 0;
  }

  public byte remove()
  {
    if (isEmpty())
      throw new IllegalStateException("The buffer is already empty");
    byte b = this.buffer[this.head];
    this.head = (1 + this.head);
    if (this.head >= this.buffer.length)
      this.head = 0;
    return b;
  }

  public int size()
  {
    if (this.tail < this.head)
      return this.buffer.length - this.head + this.tail;
    return this.tail - this.head;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     org.apache.james.mime4j.decoder.UnboundedFifoByteBuffer
 * JD-Core Version:    0.6.2
 */