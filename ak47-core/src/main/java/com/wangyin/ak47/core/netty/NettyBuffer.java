package com.wangyin.ak47.core.netty;

import com.wangyin.ak47.core.Buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;;

/**
 * 对 ByteBuf代理。
 * 
 * @author wyhanyu
 *
 */
public class NettyBuffer implements Buffer {
    
    private ByteBuf byteBuf;
    
    
    public NettyBuffer(){
        //this.byteBuf = Unpooled.buffer();
        this.byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
    }
    
    public NettyBuffer(ByteBuf byteBuf){
        this.byteBuf = byteBuf;
    }
    
    public void setByteBuf(ByteBuf byteBuf){
        this.byteBuf = byteBuf;
    }
    
    public ByteBuf getByteBuf(){
        return byteBuf;
    }
    
    @Override
    public boolean isReadable() {
        return byteBuf.isReadable();
    }

    @Override
    public boolean isWritable() {
        return byteBuf.isWritable();
    }

    @Override
    public int capacity() {
        return byteBuf.capacity();
    }

    @Override
    public Buffer clear() {
        byteBuf.clear();
        return this;
    }

    @Override
    public int readerIndex() {
        return byteBuf.readerIndex();
    }

    @Override
    public Buffer readerIndex(int readerIndex) {
        byteBuf.readerIndex(readerIndex);
        return this;
    }

    @Override
    public int writerIndex() {
        return byteBuf.writerIndex();
    }

    @Override
    public Buffer writerIndex(int writerIndex) {
        byteBuf.writerIndex(writerIndex);
        return this;
    }

    @Override
    public int readableBytes() {
        return byteBuf.readableBytes();
    }

    @Override
    public int writableBytes() {
        return byteBuf.writableBytes();
    }

    @Override
    public Buffer markReaderIndex() {
        byteBuf.markReaderIndex();
        return this;
    }

    @Override
    public Buffer resetReaderIndex() {
        byteBuf.resetReaderIndex();
        return this;
    }

    @Override
    public Buffer markWriterIndex() {
        byteBuf.markWriterIndex();
        return this;
    }

    @Override
    public Buffer resetWriterIndex() {
        byteBuf.resetWriterIndex();
        return this;
    }

    @Override
    public Buffer discardReadBytes() {
        byteBuf.discardReadBytes();
        return this;
    }

    @Override
    public Buffer readBytes(byte[] dst) {
        byteBuf.readBytes(dst);
        return this;
    }
    
    @Override
    public Buffer readBytes(byte[] dst, int dstIndex, int length) {
        byteBuf.readBytes(dst, dstIndex, length);
        return this;
    }
    
    @Override
    public Buffer readBytes(int length){
        return new NettyBuffer(byteBuf.readBytes(length));
    }


    @Override
    public Buffer skipBytes(int length) {
        byteBuf.skipBytes(length);
        return this;
    }


    @Override
    public Buffer writeBytes(byte[] src) {
        byteBuf.writeBytes(src);
        return this;
    }

    @Override
    public Buffer writeBytes(byte[] src, int srcIndex, int length) {
        byteBuf.writeBytes(src, srcIndex, length);
        return this;
    }
    
    @Override
    public Buffer writeBytes(ByteBuf src){
        byteBuf.writeBytes(src);
        return this;
    }

    @Override
    public Buffer copy() {
        NettyBuffer simpleBuf = new NettyBuffer(byteBuf.copy());
        return simpleBuf;
    }

    @Override
    public Buffer copy(int index, int length) {
        NettyBuffer simpleBuf = new NettyBuffer(byteBuf.copy(index, length));
        return simpleBuf;
    }

    @Override
    public Buffer slice() {
        NettyBuffer simpleBuf = new NettyBuffer(byteBuf.slice().retain());
        return simpleBuf;
    }

    @Override
    public Buffer slice(int index, int length) {
        NettyBuffer simpleBuf = new NettyBuffer(byteBuf.slice(index, length).retain());
        return simpleBuf;
    }

    @Override
    public Buffer duplicate() {
        NettyBuffer simpleBuf = new NettyBuffer(byteBuf.duplicate());
        return simpleBuf;
    }

    @Override
    public boolean hasArray() {
        return byteBuf.hasArray();
    }

    @Override
    public byte[] array() {
    	int readIndex=byteBuf.readerIndex();
    	byteBuf.readerIndex(0);
    	byte[] bytes=new byte[byteBuf.readableBytes()];
    	byteBuf.readBytes(bytes);
    	byteBuf.readerIndex(readIndex);
        return bytes;
    }
    
    @Override
    public boolean release(){
        return byteBuf.release();
    }

    @Override
    public byte readByte() {
        return byteBuf.readByte();
    }

    @Override
    public int readInt() {
        return byteBuf.readInt();
    }

    @Override
    public short readShort() {
        return byteBuf.readShort();
    }

    @Override
    public long readLong() {
        return byteBuf.readLong();
    }

    @Override
    public Buffer writeByte(byte value) {
        byteBuf.writeByte(value);
        return this;
    }

    @Override
    public Buffer writeShort(short value) {
        byteBuf.writeShort(value);
        return this;
    }

    @Override
    public Buffer writeInt(int value) {
        byteBuf.writeInt(value);
        return this;
    }

    @Override
    public Buffer writeLong(long value) {
        byteBuf.writeLong(value);
        return this;
    }

    
    @Override
    public byte getByte(int index) {
        return byteBuf.getByte(index);
    }

    @Override
    public short getShort(int index) {
        return byteBuf.getShort(index);
    }

    @Override
    public int getInt(int index) {
        return byteBuf.getInt(index);
    }

    @Override
    public long getLong(int index) {
        return byteBuf.getLong(index);
    }

    @Override
    public float getFloat(int index) {
        return byteBuf.getFloat(index);
    }

    @Override
    public double getDouble(int index) {
        return byteBuf.getDouble(index);
    }

    @Override
    public Buffer getBytes(int index, byte[] dst) {
        byteBuf.getBytes(index, dst);
        return this;
    }

    @Override
    public Buffer setByte(int index, int value) {
        byteBuf.setByte(index, value);
        return this;
    }

    @Override
    public Buffer setShort(int index, int value) {
        byteBuf.setShort(index, value);
        return this;
    }

    @Override
    public Buffer setInt(int index, int value) {
        byteBuf.setInt(index, value);
        return this;
    }

    @Override
    public Buffer setLong(int index, long value) {
        byteBuf.setLong(index, value);
        return this;
    }

    @Override
    public Buffer setFloat(int index, float value) {
        byteBuf.setFloat(index, value);
        return this;
    }

    @Override
    public Buffer setDouble(int index, double value) {
        byteBuf.setDouble(index, value);
        return this;
    }

    @Override
    public Buffer setBytes(int index, byte[] src) {
        byteBuf.setBytes(index, src);
        return this;
    }

//    /**
//     * Mark the begin of a data package.
//     */
//    private int rollbackIndex;
//    @Override
//    public void mark(){
//        rollbackIndex = byteBuf.readerIndex();
//    }
//    @Override
//    public void rollback(){ 
//        byteBuf.readerIndex(rollbackIndex);
//    }
    
    
}
