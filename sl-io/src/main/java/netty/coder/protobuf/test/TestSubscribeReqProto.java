package netty.coder.protobuf.test;

import com.google.protobuf.InvalidProtocolBufferException;
import netty.coder.protobuf.SubscribeReqProto;

public class TestSubscribeReqProto {
    public static byte[] encode(SubscribeReqProto.SubscribeReq req){
        return req.toByteArray();
    }
    public static SubscribeReqProto.SubscribeReq  decode(byte[] bytes) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(bytes);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq(){
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqId(1);
        builder.setUserName("luhai");
        builder.setProductName("test proto");
        builder.setAddress("hefei");
        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        SubscribeReqProto.SubscribeReq req1 = decode(encode(req));
        System.out.println(" after decode :" + req1.toString());
        System.out.println("assert equal :" + req.equals(req1));
    }
}
