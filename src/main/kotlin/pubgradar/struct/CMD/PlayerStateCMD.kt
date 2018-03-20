package pubgradar.struct.CMD

import pubgradar.GameListener
import pubgradar.deserializer.channel.ActorChannel.Companion.attacks
import pubgradar.deserializer.channel.ActorChannel.Companion.selfID
import pubgradar.deserializer.channel.ActorChannel.Companion.selfStateID
import pubgradar.deserializer.channel.ActorChannel.Companion.uniqueIds
import pubgradar.register
import pubgradar.struct.*
import pubgradar.struct.Item.Companion.simplify
import pubgradar.util.tuple2
import java.util.concurrent.ConcurrentHashMap
import pubg.radar.LogLevel.*
import pubg.radar.debugln
import pubg.radar.infoln


val playerNames=ConcurrentHashMap<NetworkGUID,String>()
val playerNumKills= ConcurrentHashMap<NetworkGUID,Int>()

object PlayerStateCMD: GameListener {
    init {
        register(this)
    }

    override fun onGameOver() {
        uniqueIds.clear()
        attacks.clear()
        selfID = NetworkGUID(0)
        selfStateID = NetworkGUID(0)
        playerNumKills.clear()
    }
  fun process(actor:Actor,bunch:Bunch,repObj:NetGuidCacheObject?,waitingHandle:Int,data:HashMap<String,Any?>):Boolean {



      try {
          actor as PlayerState
          with(bunch) {
              debugln{("WAITING HANDLE; $waitingHandle")}
              when (waitingHandle) {
                  16 -> {
                      val score = propertyFloat()
         debugln{("score=$score")}
                  }
                  17 -> {
                      val ping = propertyByte()
                  }
                  18 -> {
                      val name = propertyString()
                      actor.name = name
                      debugln{("ACTOR NAME: ${actor.netGUID} playerID=$name")}
                  }
                  19 -> {
                      val playerID = propertyInt()
                      debugln{("${actor.netGUID} playerID=$playerID")}
                  }
                  20 -> {
                      val bFromPreviousLevel = propertyBool()
                      debugln{("${actor.netGUID} bFromPreviousLevel=$bFromPreviousLevel")}
                  }
                  21 -> {
                      val isABot = propertyBool()
                          debugln{("${actor.netGUID} isABot=$isABot")}
                  }
                  22 -> {
                      val bIsInactive = propertyBool()
                          debugln{("${actor.netGUID} bIsInactive=$bIsInactive")}
                  }
                  23 -> {
                      val bIsSpectator = propertyBool()
                          debugln{("${actor.netGUID} bIsSpectator=$bIsSpectator")}
                  }
                  24 -> {
                      val bOnlySpectator = propertyBool()
                          debugln{("${actor.netGUID} bOnlySpectator=$bOnlySpectator")}
                  }
                  25 -> {
                      val StartTime = propertyInt()
                      debugln{("${actor.netGUID} StartTime=$StartTime")}
                  }
                  26 -> {
                      val uniqueId = propertyNetId()
                      uniqueIds[uniqueId] = actor.netGUID
                          debugln{("${playerNames[actor.netGUID]}${actor.netGUID} uniqueId=$uniqueId")}
                  }
                  27 -> {//indicate player's death
                      val Ranking = propertyInt()
                          debugln{("${playerNames[actor.netGUID]}${actor.netGUID} Ranking=$Ranking")}
                  }
                  28 -> {
                      val AccountId = propertyString()
                              debugln{("${actor.netGUID} AccountId=$AccountId")}
                  }
                  29 -> {
                      val ReportToken = propertyString()
                  }
                  30 -> {//ReplicatedCastableItems
                      val arraySize = readUInt16()
                      actor.castableItems.resize(arraySize)
                      var index = readIntPacked()
                      while (index != 0) {
                          val idx = index - 1
                          val arrayIdx = idx / 3
                          val structIdx = idx % 3
                          val element = actor.castableItems[arrayIdx] ?: tuple2("", 0)
                          when (structIdx) {
                              0 -> {
                                  val (guid, castableItemClass) = readObject()
                                  if (castableItemClass != null)
                                      element._1 = simplify(castableItemClass.pathName)
                              }
                              1 -> {
                                  val ItemType = readInt(8)
                                  val a = ItemType
                              }
                              2 -> {
                                  val itemCount = readInt32()
                                  element._2 = itemCount
                              }
                          }
                          actor.castableItems[arrayIdx] = element
                          index = readIntPacked()
                      }
                      return true
                  }
                  31 -> {
                      val ObserverAuthorityType = readInt(4)
                  }
                  32 -> {
                      val teamNumber = readInt(100)
                      actor.teamNumber = teamNumber
                  }
                  33 -> {
                      val bIsZombie = propertyBool()
                  }
                  34 -> {
                      val scoreByDamage = propertyFloat()
                      debugln{("SCORE BY DAMAGE: $scoreByDamage")}
                  }
                  35 -> {
                      val ScoreByKill = propertyFloat()
                      debugln{("SCORE BY KILL: $ScoreByKill")}
                  }
                  36 -> {
                      val ScoreByRanking = propertyFloat()
                          debugln{("SCORE BY RANKING: $ScoreByRanking")}
                  }
                  37 -> {

                      val ScoreFactor = propertyFloat()
                          debugln{("SCORE FACTOR: $ScoreFactor")}
                  }
                  38 -> {
                      val NumKills = propertyInt()
                          debugln{("NUM KILLS: $NumKills")}
                      // actor.numKills = NumKills
                      playerNumKills[actor.netGUID] = NumKills

                  }
                  39 -> {
                      val TotalMovedDistanceMeter = propertyFloat()
                      debugln{("TOTAL MOVED DISTANCE: $TotalMovedDistanceMeter")}
                      selfStateID = actor.netGUID//only self will get this update
                  }
                  40 -> {
                      val TotalGivenDamages = propertyFloat()
                          debugln{("TOTAL GIVEN DAMAGE: $TotalGivenDamages")}
                  }
                  41 -> {
                      val LongestDistanceKill = propertyFloat()
                          debugln{("LONGEST KILL:  $LongestDistanceKill")}
                  }
                  42 -> {
                      val HeadShots = propertyInt()
                          debugln{("HEADSHOTS: $HeadShots")}
                  }
                  43 -> {//ReplicatedEquipableItems
                      try {
                          val arraySize = readUInt16()
                          actor.equipableItems.resize(arraySize)
                          var index = readIntPacked()
                          while (index != 0) {
                              val idx = index - 1
                              val arrayIdx = idx / 2
                              val structIdx = idx % 2
                              val element = actor.equipableItems[arrayIdx] ?: tuple2("", 0f)
                              when (structIdx) {
                                  0 -> {
                                      val (guid, equipableItemClass) = readObject()
                                      if (equipableItemClass != null)
                                          element._1 = simplify(equipableItemClass.pathName)
                                      val a = guid
                                  }
                                  1 -> {
                                      val durability = readFloat()
                                      element._2 = durability
                                      val a = durability
                                  }
                              }
                              actor.equipableItems[arrayIdx] = element
                              index = readIntPacked()
                          }
                          return true
                      } catch (e: Exception) {
                          debugln{("PlayerState is throwing on 43: $e ${e.stackTrace} ${e.message}")}
                      }

                  }
                  44 -> {
                      val bIsInAircraft = propertyBool()
                  }
                  45 -> {//LastHitTime
                      val lastHitTime = propertyFloat()
                  }
                  46 -> {
                      val currentAttackerPlayerNetId = propertyString()
                      attacks.add(tuple2(uniqueIds[currentAttackerPlayerNetId]!!, actor.netGUID))
                  }
                  else -> return ActorCMD.process(actor, bunch, repObj, waitingHandle, data)
              }
          }
          return true
      }
      catch (e: Exception){
          debugln{("PlayerState is throwing somewhere: $e ${e.stackTrace} ${e.message}")}
      }
      return false
  }
}