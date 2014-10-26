package com.wangyin.ak47.core.netty;

//import java.net.SocketAddress;
//import java.util.concurrent.ScheduledExecutorService;
//
//import com.wangyin.qa.ak47.common.Logger;
//import com.wangyin.qa.ak47.core.Channel;
//import com.wangyin.qa.ak47.core.PipeFuture;
//import com.wangyin.qa.ak47.core.Message;
//import com.wangyin.qa.ak47.core.buffer.Buffer;
//import com.wangyin.qa.ak47.core.buffer.SimpleBuffer;
//import com.wangyin.qa.ak47.core.message.SimpleMessage;
//import com.wangyin.qa.ak47.core.util.SimplePipeFuture;
//import com.wangyin.qa.ak47.core.util.SimplePipeHandlerContext;
//import com.wangyin.qa.ak47.exception.AK47RuntimeException;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelHandler;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelPromise;

///**
// * 连接Netty和AK47
// * 
// * @author wyhanyu
// *
// * @param <O>   Outbound
// * @param <I>   Inbound
// */
//public class Netty5ChannelHandlerAdapter<O, I> implements ChannelHandler {
//    private static final Logger log = new Logger(Netty5ChannelHandlerAdapter.class);
//    
//    private SimplePipeHandlerContext<O, I> pipeHandlerCtx;
//    private ChannelHandlerContext nettyCtx;
//    
//    public Netty5ChannelHandlerAdapter(SimplePipeHandlerContext<O, I> pipeHandlerCtx){
//        this.pipeHandlerCtx = pipeHandlerCtx;
//    }
//    
//    
//    
//    /**
//     * 
//     * @return
//     */
//    public PipeFuture close(){
//        ChannelFuture future = nettyCtx.close();
//        SimplePipeFuture pfuture = new SimplePipeFuture(pipeHandlerCtx, future);
//        return pfuture;
//    }
//    
//    /**
//     * fix netty bug: fire write dont contains itself.
//     * @param pojo
//     */
//    public PipeFuture send(O pojo){
//        
//        Message<O> msg = new SimplePipeMessage<O>();
//        msg.setDirection(true);
//        msg.setBuffer(newBuffer());
//        msg.setPojo(pojo);
//        
//        return send(msg);
//    }
//    
//    /**
//     * send buffer
//     * 
//     * @param buf
//     * @return
//     */
//    public PipeFuture send(Buffer buf){
//        
//        Message<O> msg = new SimplePipeMessage<O>();
//        msg.setDirection(true);
//        msg.setBuffer(buf);
//        
//        return send(msg);
//    }
//    
//    /**
//     * send msg
//     * 
//     * @param msg
//     * @return
//     */
//    public PipeFuture send(Message<O> msg){
//        
//        pipeHandlerCtx.fireSend(msg);
//        
//        SimpleBuffer sb = (SimpleBuffer)msg.getBuffer();
//        int writeBytes = sb.readableBytes();
//        if( writeBytes == 0 ){
//            log.warn("fireSend but write NOTHING.");
//        }
//        
//        ChannelFuture future = nettyCtx.writeAndFlush(sb.getByteBuf());
//        SimplePipeFuture pfuture = new SimplePipeFuture(pipeHandlerCtx, future);
//        log.debug("send {} bytes.", writeBytes);
//        return pfuture;
//    }
//    
//
//    public ScheduledExecutorService executor(){
//        if( null == nettyCtx ){
//            throw new AK47RuntimeException("ChannelHandlerContext is NOT initialized.");
//        }
//        return nettyCtx.executor();
//    }
//    
//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        this.nettyCtx = ctx;
//    }
//
//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//        // NOTHING
//    }
//    
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
//            throws Exception {
//        pipeHandlerCtx.fireCaught(cause);
//    }
//
//    @Override
//    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        //FIXME: channelRegistered is not handlerRegistered
//        pipeHandlerCtx.fireRegistered();
//    }
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        pipeHandlerCtx.fireConnected();
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        pipeHandlerCtx.fireDisconnected();
//    }
//
//    
//    private SimpleBuffer inBuf;
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg)
//            throws Exception {
//        if( msg instanceof ByteBuf ){
//            ByteBuf bb = (ByteBuf) msg;
//            if( null == inBuf ){
//                inBuf = new SimpleBuffer(bb);
//            }else{
//                inBuf.writeBytes(bb);
//                bb.release();
//            }
//            
//            Message<I> pm = new SimplePipeMessage<I>();
//            pm.setBuffer(inBuf);
//            pm.setDirection(false);
//            
//            pipeHandlerCtx.fireReceived(pm);
//            if( inBuf.readableBytes() <= 0 ){
//                inBuf.release();
//                inBuf = null;
//            }
//        }else{
//            log.warn("msg is NOT a ByteBuf.");
//            ctx.fireChannelRead(msg);
//        }
//    }
//
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.fireChannelReadComplete();
//    }
//
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
//            throws Exception {
//        ctx.fireUserEventTriggered(evt);
//    }
//
//    @Override
//    public void channelWritabilityChanged(ChannelHandlerContext ctx)
//            throws Exception {
//        ctx.fireChannelWritabilityChanged();
//    }
//
//    @Override
//    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress,
//            ChannelPromise promise) throws Exception {
//        log.debug("=========== NettyChannelHandlerAdapter bind.");
//        ctx.bind(localAddress, promise);
//    }
//
//    @Override
//    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
//            SocketAddress localAddress, ChannelPromise promise)
//            throws Exception {
//        log.debug("=========== NettyChannelHandlerAdapter connect.");
//        ctx.connect(remoteAddress, localAddress, promise);
//    }
//
//    @Override
//    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise)
//            throws Exception {
//        log.debug("=========== NettyChannelHandlerAdapter disconnect.");
//        ctx.disconnect(promise);
//    }
//
//    @Override
//    public void close(ChannelHandlerContext ctx, ChannelPromise promise)
//            throws Exception {
//        log.debug("=========== NettyChannelHandlerAdapter close.");
//        ctx.close(promise);
//    }
//
//    @Override
//    public void read(ChannelHandlerContext ctx) throws Exception {
//        log.debug("=========== NettyChannelHandlerAdapter read.");
//        ctx.read();
//    }
//
//    
//    @Override
//    public void write(ChannelHandlerContext ctx, Object msg,
//            ChannelPromise promise) throws Exception {
//        log.debug("=========== NettyChannelHandlerAdapter write.");
//        ctx.write(msg, promise);
//        
//    }
//
//    @Override
//    public void flush(ChannelHandlerContext ctx) throws Exception {
//        log.debug("=========== NettyChannelHandlerAdapter flush.");
//        ctx.flush();
//    }
//
//	/**
//	 * create new Buffer
//	 * @return
//	 */
//	public Buffer newBuffer() {
////		return new SimpleBuffer(nettyCtx.alloc().buffer());
//		return new SimpleBuffer();
//	}
//	
//	
//	
//	/**
//	 * 返回channelId的字符串形式
//	 * 
//	 * 注：Channel代表着一个connection，不管是用Netty还是
//	 * @return
//	 */
//	public Channel channel() {
//	    return new NettyChannel(nettyCtx.channel());
//	}
//    
//}
