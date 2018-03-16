package pubg.radar.struct

import pubg.radar.deserializer.shortRotationScale
import pubg.radar.struct.cmd.selfCoords
import pubg.radar.struct.cmd.selfDirection

fun charmovecomp(bunch: Bunch) {
    val repIndex = bunch.readInt(144)
//  println(repIndex)
    val payloadBits = bunch.readIntPacked()
    val rpcPayload = bunch.deepCopy(payloadBits)
    bunch.skipBits(payloadBits)
    when (repIndex) {
        29 -> {//void ServerMove(float TimeStamp, const struct FVector_NetQuantize10& InAccel, const struct FVector_NetQuantize100& ClientLoc, unsigned char CompressedMoveFlags, unsigned char ClientRoll, uint32_t View, class UPrimitiveComponent* ClientMovementBase, const struct FName& ClientBaseBoneName, unsigned char ClientMovementMode);
            if (rpcPayload.readBit()) {
                val timeStamp = rpcPayload.readFloat()
            }
            if (rpcPayload.readBit()) {
                val inAccel = rpcPayload.readVector(10, 24)
            }
            if (rpcPayload.readBit()) {
                val clientLoc = rpcPayload.readVector(100, 30)
                selfCoords.set(clientLoc.x, clientLoc.y)
            }
            if (rpcPayload.readBit()) {
                val compressedMoveFlags = rpcPayload.readUInt8()
            }
            if (rpcPayload.readBit()) {
                val clientRoll = rpcPayload.readUInt8()
            }
            if (rpcPayload.readBit()) {
                val view = rpcPayload.readUInt32()
                selfDirection = (view shr 16) * shortRotationScale
            }
        }
        30 -> {//void ServerMoveDual(float TimeStamp0, const struct FVector_NetQuantize10& InAccel0, unsigned char PendingFlags, uint32_t View0, float TimeStamp, const struct FVector_NetQuantize10& InAccel, const struct FVector_NetQuantize100& ClientLoc, unsigned char NewFlags, unsigned char ClientRoll, uint32_t View, class UPrimitiveComponent* ClientMovementBase, const struct FName& ClientBaseBoneName, unsigned char ClientMovementMode);

            if (rpcPayload.readBit()) {
                val timeStamp = rpcPayload.readFloat()
            }
            if (rpcPayload.readBit()) {
                val inAccel = rpcPayload.readVector(10, 24)
//        print("inAccel:(${inAccel.x},${inAccel.y}),")
            }
            if (rpcPayload.readBit()) {
                val PendingFlags = rpcPayload.readUInt8()
//        print("compressedMoveFlags:$compressedMoveFlags,")
            }
            if (rpcPayload.readBit()) {
                val view = rpcPayload.readUInt32()
//        print("view:$view")
            }

            if (rpcPayload.readBit()) {
                val timeStamp = rpcPayload.readFloat()
            }
            if (rpcPayload.readBit()) {
                val inAccel = rpcPayload.readVector(10, 24)
//        print("inAccel:(${inAccel.x},${inAccel.y}),")
            }
            if (rpcPayload.readBit()) {
                val clientLoc = rpcPayload.readVector(100, 30)
                selfCoords.set(clientLoc.x, clientLoc.y)
//        print("clientLoc:(${clientLoc.x},${clientLoc.y}),")
            }
            if (rpcPayload.readBit()) {
                val compressedMoveFlags = rpcPayload.readUInt8()
//        print("compressedMoveFlags:$compressedMoveFlags,")
            }
            if (rpcPayload.readBit()) {
                val clientRoll = rpcPayload.readUInt8()
//        print("clientRoll:$clientRoll,")
            }
            if (rpcPayload.readBit()) {
                val view = rpcPayload.readUInt32()
                selfDirection = (view shr 16) * shortRotationScale
            }
        }
        else -> {
        }
    }
}