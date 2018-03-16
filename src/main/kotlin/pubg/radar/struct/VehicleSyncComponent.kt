package pubg.radar.struct


fun VehicleSyncComponent(bunch: Bunch) {
    val repIndex = bunch.readInt(8)
//  println(repIndex)
    val payloadBits = bunch.readIntPacked()
    val rpcPayload = bunch.deepCopy(payloadBits)
    bunch.skipBits(payloadBits)
    when (repIndex) {
        5 -> {
            if (rpcPayload.readBit()) { //inCorrectionId: Int?,
                val timeStamp = rpcPayload.readInt()
                println("Time: $timeStamp")
            }
            if (rpcPayload.readBit()) { // clientLoc : VectorNetQuantize100?,
                val clientLoc = rpcPayload.readVector(100, 30)
                println("Client Loc: $clientLoc")
                // selfCoords.set(clientLoc.x, clientLoc.y)
            }
            if (rpcPayload.readBit()) { //clientLinearVelocity : VectorNetQuantize100?,
                val cLinVelocity = rpcPayload.readVector(100, 30)
                println("clientLinearVelocity: $cLinVelocity")
            }
            if (rpcPayload.readBit()) { //clientRotator : VectorNetQuantizeNormal?,
                val clientRotator = rpcPayload.readVector(10, 24)
                println("clientRotator: $clientRotator")
            }
            if (rpcPayload.readBit()) { // clientAngularVelocity : VectorNetQuantize100?
                val cAngVelocity = rpcPayload.readVector(100, 30)
                println("clientAngularVelocity: $cAngVelocity")
            } else {
                //     println()
                //check(rpcPayload.bitsLeft()==0)
            }
        }
    }
}