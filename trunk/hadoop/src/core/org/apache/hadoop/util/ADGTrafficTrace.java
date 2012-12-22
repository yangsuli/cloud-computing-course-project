/*
 * This class contains all the information we use to describle each kind of traffic
 * yangsuli 11/25/2012
 */

package org.apache.hadoop.util;

import javax.net.SocketFactory;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.lang.String;
import java.lang.RuntimeException;
import java.net.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ADGTrafficTrace {

    public static final Log LOG = LogFactory.getLog(ADGTrafficTrace.class);

    /*
     * These two share the same flow
     * refer to DFSClient.java BlockReader related code
     */
    public static final byte TRAFFIC_READ_DATA_ACK = (byte) 11;
    public static final byte TRAFFIC_READ_DATA_REQUEST = (byte) 12;

    //this conresponds to category 6 in google doc
    public static final byte TRAFFIC_GROUP_READ_REQACK = (byte) 201;

    /*
     * The following share a socket(flow)
     */
    public static final byte TRAFFIC_READ_DATA_BLOCK = (byte) 13;
    //this conresponds to category 6 in google doc
    public static final byte TRAFFIC_GROUP_READ_TRANSFER = (byte) 202;

    /*
     * The following share a socket(flow)
     */
    public static final byte TRAFFIC_WRITE_DATA_ACK = (byte) 14;
    //this conresponds to category 7 in google doc
    public static final byte TRAFFIC_GROUP_WRITE_ACK = (byte) 203;

    /*
     * The following share a socket(flow)
     * From client to datanode
     */
    public static final byte TRAFFIC_BLOCK_CHECKSUM = (byte) 119;
    public static final byte TRAFFIC_GROUP_BLOCK_CHECKSUM = (byte) 233;

    /*
     * The following share a socket (flow)
     * It is the client-to-first-datanode transfer when client writes a block
     */
    public static final byte TRAFFIC_WRITE_CLIENT_DATA_HEADER = (byte) 15;
    public static final byte TRAFFIC_WRITE_CLIENT_DATA_PACKETS = (byte) 16;
    //this conresponds to category 7 in google doc
    public static final byte TRAFFIC_GROUP_WRITE_TRANSFER = (byte) 204;

    /*
     * The following share a socket (flow)
     * It is the pipelined data transfer when client writes a block
     */
    public static final byte TRAFFIC_WRITE_PIPELINE_DATA_HEADER = (byte) 17;
    public static final byte TRAFFIC_WRITE_PIPELINE_DATA_PACKETS = (byte) 18;
    //this conresponds to category 7 in google doc
    public static final byte TRAFFIC_GROUP_WRITE_PIPELINE = (byte) 205;

    /*
     * The following is data transfer between datanodes
     * I am not quite sure what this is responsible for
     * maybe for re-replication? 
     * Anyway, this seems like a one-way transfer
     * Receiver doesn't even send any kind of ack
     * yangsuli 12/19/2012
     */
    public static final byte TRAFFIC_DN_TRANSFER_HEADER = (byte) 145;
    //this includes block checksum
    public static final byte TRAFFIC_DN_TRANSFER_BLOCK = (byte) 146;

    public static final byte TRAFFIC_GROUP_DN_DATA_TRANSFER = (byte) 238;

    /*
     * The following RPC traffic share the same socket/connection pool
     */

    /*
     * The following traffic are basically DatanodeCommands and their replies.
     * Detailed Description of each kind of traffic can be found in the comment of DatanodeProtocol.java
     * SEND_TRAFFIC are initiated from the datanode side
     * While RESPOND_TRAFFIC are initiated from the namenode side
     */

    /**
     * sendHeartbeat() tells the NameNode that the DataNode is still
     * alive and well.  Includes some status info, too. 
     * It also gives the NameNode a chance to return 
     * an array of "DatanodeCommand" objects.
     * A DatanodeCommand tells the DataNode to invalidate local block(s), 
     * or to copy them to other DataNodes, etc.
     */
    public static final byte TRAFFIC_RPC_SEND_HEARTBEAT = (byte) 19;
    //This actually contains an array of block-oriented commands for datanode to execute
    public static final byte TRAFFIC_RPC_RESPOND_HEARTBEAT = (byte) 20; 
    //this conresponds to category 5 in google doc
    public static final byte TRAFFIC_GROUP_RPC_HEARTBEAT = (byte) 206;
    public static final byte TRAFFIC_GROUP_RPC_HEARTBEAT_RESPOND = (byte) 207;


    //traffic only happens at system start up, thus deemed not important
    public static final byte TRAFFIC_RPC_STARTUP_INIT = (byte) 21;
    public static final byte TRAFFIC_RPC_RESPOND_STARTUP_INIT = (byte) 22;

    /*
     * Debugging or Error dealint Traffic. Not very interesting to us right now
     */
    public static final byte TRAFFIC_RPC_ERROR = (byte) 27;
    public static final byte TRAFFIC_RPC_ERROR_RESPOND = (byte) 28;
    /*
     * Upgrade or maitains traffic
     * Not very interesting to us right now
     */
    public static final byte TRAFFIC_RPC_UPGRATE_MAINTAIN = (byte) 29; 
    public static final byte TRAFFIC_RPC_UPGRATE_MAINTAIN_RESPOND = (byte) 30; 

    /*
     * Both blockReprot and blockReceived are dananodes reporting
     * its recent status to namenode
     */
    public static final byte TRAFFIC_RPC_BLOCK_REPORT = (byte)23;
    public static final byte TRAFFIC_RPC_RESPOND_BLOCK_REPORT = (byte)24;
    //this conresponds to category 4 in google doc
    public static final byte TRAFFIC_GROUP_RPC_BLOCK_REPORT = (byte) 208;
    public static final byte TRAFFIC_GROUP_RPC_BLOCK_REPORT_RESPOND = (byte) 209;


    public static final byte TRAFFIC_RPC_SEND_BLOCK_RECEIVED = (byte)25;
    public static final byte TRAFFIC_RPC_RESPOND_BLOCK_RECIVED = (byte)26;

    //Doesn't seem we have a category for this in our orignal google doc
    public static final byte TRAFFIC_GROUP_DN_RPC_OTHER = (byte) 212;
    public static final byte TRAFFIC_GROUP_DN_RPC_OTHER_RESPOND = (byte) 213;

    /*
     * FIXME:
     * THis could be called by client too
     * Needs to differenciate
     * But for now those are all error traffic that we don't care
     * Thus now we dont uses these, instead they go to error traffic, I think
     * yangsuli 12/6/2012
     */
    public static final byte TRAFFIC_RPC_BAD_BLOCK_REPORT = (byte)31;
    public static final byte TRAFFIC_RPC_RESPOND_BAD_BLOCK_REPORT = (byte)32;


    /*
     * Request metadata of a block from the namenode
     */
    public static final byte TRAFFIC_RPC_REQUEST_BLOCK_METADATA = (byte)33;
    public static final byte TRAFFIC_RPC_REQUEST_BLOCK_METADATA_RESPOND = (byte)34;

    /*
     * Datanode report commit of a sychrouse operaton on block
     * I think
     * yangsuli 12/6/2012
     */
    public static final byte TRAFFIC_RPC_COMMIT_BLOCK_SYNC = (byte)35; 
    public static final byte TRAFFIC_RPC_COMMIT_BLOCK_SYNC_RESPOND = (byte)36; 

    /*
     * FIXME:
     * This could be called either from the dananode or the client
     * Thus need a mechanism to differenciate these two
     * yangsuli 12/6/2012
     */
    public static final byte TRAFFIC_RPC_GET_VERSION = (byte)39;
    public static final byte TRAFFIC_RPC_GET_VERSION_RESPOND = (byte)40;

    //no cresponding
    public static final byte TRAFFIC_GROUP_RPC_BLOCK_OP = (byte) 210;
    public static final byte TRAFFIC_GROUP_RPC_BLOCK_OP_RESPOND = (byte) 211;

    /*
     * The following traffic are between client and the namenodes
     * Detailed Description of each kind of traffic can be found in the comment of ClientProtocol.java
     * SEND_TRAFFIC are initiated from the client side
     * While RESPOND_TRAFFIC are initiated from the namenode side
     */

    /*
     * Client request metadata
     */
    public static final byte TRAFFIC_RPC_CLIENT_REQ_BLOCK_LOC = (byte)37;
    public static final byte TRAFFIC_RPC_CLIENT_REQ_BLOCK_LOC_RESPOND = (byte)38;


    /*
     * File Operations (doesn't involve bulk data transfer
     */
    public static final byte TRAFFIC_RPC_CLIENT_CREATE_FILE = (byte)41;
    public static final byte TRAFFIC_RPC_CLIENT_CREATE_FILE_RESPOND = (byte)42;
    public static final byte TRAFFIC_RPC_CLIENT_APPEND_FILE = (byte)43;
    public static final byte TRAFFIC_RPC_CLIENT_APPEND_FILE_RESPOND = (byte)44;
    public static final byte TRAFFIC_RPC_CLIENT_SET_REPLICA_FILE = (byte)45;
    public static final byte TRAFFIC_RPC_CLIENT_SET_REPLICA_FILE_RESPOND = (byte)46;
    public static final byte TRAFFIC_RPC_CLIENT_SET_PERM_FILE = (byte)47;
    public static final byte TRAFFIC_RPC_CLIENT_SET_PERM_FILE_RESPOND = (byte)48;
    public static final byte TRAFFIC_RPC_CLIENT_SET_OWN_FILE = (byte)49;
    public static final byte TRAFFIC_RPC_CLIENT_SET_OWN_FILE_RESPOND = (byte)50;
    public static final byte TRAFFIC_RPC_CLIENT_SET_TIME_FILE = (byte)77;
    public static final byte TRAFFIC_RPC_CLIENT_SET_TIME_FILE_RESPOND = (byte)78;
    public static final byte TRAFFIC_RPC_CLIENT_ABAND_BLK = (byte)51;
    public static final byte TRAFFIC_RPC_CLIENT_ABAND_BLK_RESPOND = (byte)52;
    public static final byte TRAFFIC_RPC_CLIENT_ADD_BLK = (byte)1;
    public static final byte TRAFFIC_RPC_CLIENT_ADD_BLK_RESPOND = (byte)2;
    public static final byte TRAFFIC_RPC_CLIENT_COMPLETE_FILE = (byte)53; 
    public static final byte TRAFFIC_RPC_CLIENT_COMPLETE_FILE_RESPOND = (byte)54;
    public static final byte TRAFFIC_RPC_CLIENT_REPORT_BAD_BLK = (byte)55;
    public static final byte TRAFFIC_RPC_CLIENT_REPORT_BAD_BLK_RESPOND = (byte)56;
    //this conresponds to category 1 in google doc
    public static final byte TRAFFIC_GROUP_CLIENT_RPC_FILE_OP = (byte) 216;
    public static final byte TRAFFIC_GROUP_CLIENT_RPC_FILE_OP_RESPOND = (byte) 217;

    /*
     * fsync...
     */
    public static final byte TRAFFIC_RPC_CLIENT_FSYNC = (byte)75;
    public static final byte TRAFFIC_RPC_CLIENT_FSYNC_RESPOND = (byte)76;
    //this conresponds to category 1 in google doc
    public static final byte TRAFFIC_GROUP_CLIENT_RPC_FSYNC = (byte) 214;
    public static final byte TRAFFIC_GROUP_CLIENT_RPC_FSYNC_RESPOND = (byte) 215;

    /*
     * Get File Info and such
     */
    public static final byte TRAFFIC_RPC_CLIENT_GET_INFO_FILE = (byte)71;
    public static final byte TRAFFIC_RPC_CLIENT_GET_INFO_FILE_RESPOND = (byte)72;
    public static final byte TRAFFIC_RPC_CLIENT_GET_CONTENT_SUM = (byte)73;
    public static final byte TRAFFIC_RPC_CLIENT_GET_CONTENT_SUM_RESPOND = (byte)74;

    //this conresponds to category 1 in google doc
    public static final byte TRAFFIC_GROUP_RPC_CLIENT_GET_METADATA = (byte) 218;
    public static final byte TRAFFIC_GROUP_RPC_CLIENT_GET_METADATA_RESPOND = (byte) 219;



    /*
     * Client do namespace management
     */
    public static final byte TRAFFIC_RPC_CLIENT_RENAME_PATH = (byte)57;
    public static final byte TRAFFIC_RPC_CLIENT_RENAME_PATH_RESPOND = (byte)58;
    public static final byte TRAFFIC_RPC_CLIENT_DEL_PATH = (byte)3;
    public static final byte TRAFFIC_RPC_CLIENT_DEL_PATH_RESPOND = (byte)4;
    public static final byte TRAFFIC_RPC_CLIENT_MKDIR = (byte)59;
    public static final byte TRAFFIC_RPC_CLIENT_MKDIR_RESPOND = (byte)60;

    public static final byte TRAFFIC_RPC_CLIENT_GET_DIR_LIST = (byte)61;
    public static final byte TRAFFIC_RPC_CLIENT_GET_DIR_LIST_RESPOND = (byte)62;
    //this conresponds to category 1 in google doc
    public static final byte TRAFFIC_GROUP_CLIENT_RPC_NAMESPACE = (byte) 220;
    public static final byte TRAFFIC_GROUP_CLIENT_RPC_NAMESPACE_RESPOND = (byte) 221;

    /*
     * Client Lease Related
     */
    public static final byte TRAFFIC_RPC_CLIENT_RENEW_LEASE = (byte)63;
    public static final byte TRAFFIC_RPC_CLIENT_RENEW_LEASE_RESPOND = (byte)64;
    //this conresponds to category 1 in google doc
    public static final byte TRAFFIC_GROUP_CLIENT_RPC_LEASE = (byte) 223;
    public static final byte TRAFFIC_GROUP_CLIENT_RPC_LEASE_RESPOND = (byte) 224;


    /*
     * Client Get System Stats
     */
    public static final byte TRAFFIC_RPC_CLIENT_GET_STAT = (byte)65;
    public static final byte TRAFFIC_RPC_CLIENT_GET_STAT_RESPOND = (byte)66;
    public static final byte TRAFFIC_RPC_CLIENT_GET_DN_REPORT = (byte)67;
    public static final byte TRAFFIC_RPC_CLIENT_GET_DN_REPORT_RESPOND = (byte)68;
    public static final byte TRAFFIC_RPC_CLIENT_GET_PREF_BLK_SIZE = (byte)69;
    public static final byte TRAFFIC_RPC_CLIENT_GET_PREF_BLK_SIZE_RESPOND = (byte)70;

    //this conresponds to category 1 in google doc
    public static final byte TRAFFIC_GROUP_CLIENT_RPC_STAT = (byte) 225;
    public static final byte TRAFFIC_GROUP_CLIENT_RPC_STAT_RESPOND = (byte) 226;

    /*
     * Client Side Security Stuff
     */
    public static final byte TRAFFIC_RPC_CLIENT_GET_TOKEN = (byte)79; public static final byte TRAFFIC_RPC_CLIENT_GET_TOKEN_RESPOND = (byte)80;
    public static final byte TRAFFIC_RPC_CLIENT_RENEW_TOKEN = (byte)81;
    public static final byte TRAFFIC_RPC_CLIENT_RENEW_TOKEN_RESPOND = (byte)82;
    public static final byte TRAFFIC_RPC_CLIENT_CANCLE_TOKEN = (byte)83;
    public static final byte TRAFFIC_RPC_CLIENT_CANCLE_TOKEN_RESPOND = (byte)84;
    //this conresponds to category 1 in google doc
    public static final byte TRAFFIC_GROUP_CLIENT_RPC_SECURITY = (byte) 227;
    public static final byte TRAFFIC_GROUP_CLIENT_RPC_SECURITY_RESPOND = (byte) 228;

    /*
     * Coummunication between secondary NameNode and Namenode
     */
    public static final byte TRAFFIC_2ND_GET_FS_IMG = (byte) 149;
    public static final byte TRAFFIC_2ND_GET_EDIT_IMG = (byte) 148;
    public static final byte TRAFFIC_GROUP_2ND_GET_FS_IMG = (byte) 243;
    public static final byte TRAFFIC_GROUP_2ND_GET_EDIT_IMG = (byte) 244;

    public static final byte TRAFFIC_RPC_2ND_GET_BLOCK = (byte)85;
    public static final byte TRAFFIC_RPC_2ND_GET_BLOCK_RESPOND = (byte)86;
    public static final byte TRAFFIC_RPC_2ND_GET_BLOCK_KEYS = (byte)87;
    public static final byte TRAFFIC_RPC_2ND_GET_BLOCK_KEYS_RESPOND = (byte)88;
    public static final byte TRAFFIC_RPC_2ND_GET_LOG_SIZE = (byte)89;
    public static final byte TRAFFIC_RPC_2ND_GET_LOG_SIZE_RESPOND = (byte)90; 
    public static final byte TRAFFIC_RPC_2ND_ROLL_LOG = (byte)91;
    public static final byte TRAFFIC_RPC_2ND_ROLL_LOG_RESPOND = (byte)92; 
    public static final byte TRAFFIC_RPC_2ND_ROLL_FSIMG = (byte)93;
    public static final byte TRAFFIC_RPC_2ND_ROLL_FSIMG_RESPOND = (byte)94;

    //this conresponds to category 2 in google doc
    public static final byte TRAFFIC_GROUP_RPC_CHECKPOINT = (byte) 229;
    public static final byte TRAFFIC_GROUP_RPC_CHECKPOINT_RESPOND = (byte) 230;

    /*
     * General Security Stuff
     * I am not quite sure if HDFS ever use it, even though NameNode has implemeted it
     * yangsuli 12/8/2012
     */
    public static final byte TRAFFIC_RPC_SECURITY_REFRESH = (byte)95;
    public static final byte TRAFFIC_RPC_SECURITY_REFRESH_RESPOND = (byte)96;



    public static final byte TRAFFIC_RPC_UNKNOWN = (byte)97;
    public static final byte TRAFFIC_RPC_UNKNOWN_RESPOND = (byte)98;

    /*
     * Some traffic other than the HDFS traffic
     * For now we have the periodic heartbeat  
     * between the TaskTracker and the JobTracker.
     * There might be others.
     * FIXME:
     * Can we just run HDFS without any other componenets?
     * yangsuli 12/8/2012
     */
    public static final byte TRAFFIC_RPC_OTHER = (byte)99;
    public static final byte TRAFFIC_RPC_OTHER_RESPOND = (byte)100;


    public static final byte TRAFFIC_GROUP_RPC_OTHER = (byte) 231;
    public static final byte TRAFFIC_GROUP_RPC_OTHER_RESPOND = (byte) 232;



    public static class ADGTrafficDesc {
        public ADGTrafficDesc(byte type){
            this.type = type;
        }
        protected byte type;
    }


    /*
     * Some helper function to group traffic together
     */
    
    
    public static byte ADGGroupTraffic(byte type){
        byte group;
        switch(type){
            case TRAFFIC_READ_DATA_ACK:
            case TRAFFIC_READ_DATA_REQUEST:
                group = TRAFFIC_GROUP_READ_REQACK;
                break;
            case TRAFFIC_READ_DATA_BLOCK:
                group = TRAFFIC_GROUP_READ_TRANSFER;
                break;
            case TRAFFIC_WRITE_DATA_ACK:
                group = TRAFFIC_GROUP_WRITE_ACK;
                break;
            case TRAFFIC_BLOCK_CHECKSUM:
                group = TRAFFIC_GROUP_BLOCK_CHECKSUM;
                break;
            case TRAFFIC_WRITE_CLIENT_DATA_HEADER:
            case TRAFFIC_WRITE_CLIENT_DATA_PACKETS:
                group = TRAFFIC_GROUP_WRITE_TRANSFER;
                break;
            case TRAFFIC_WRITE_PIPELINE_DATA_HEADER:
            case TRAFFIC_WRITE_PIPELINE_DATA_PACKETS:
                group = TRAFFIC_GROUP_WRITE_PIPELINE;
                break;
	    case TRAFFIC_DN_TRANSFER_HEADER:
	    case TRAFFIC_DN_TRANSFER_BLOCK:
	        	group = TRAFFIC_GROUP_DN_DATA_TRANSFER;
	        	break;
            case TRAFFIC_RPC_SEND_HEARTBEAT:
                group = TRAFFIC_GROUP_RPC_HEARTBEAT;
                break;
            case TRAFFIC_RPC_RESPOND_HEARTBEAT:
                group = TRAFFIC_GROUP_RPC_HEARTBEAT_RESPOND;
                break;
            case TRAFFIC_RPC_BLOCK_REPORT:
                group = TRAFFIC_GROUP_RPC_BLOCK_REPORT;
                break;
            case TRAFFIC_RPC_RESPOND_BLOCK_REPORT:
                group = TRAFFIC_GROUP_RPC_BLOCK_REPORT_RESPOND;
                break;
            case TRAFFIC_RPC_SEND_BLOCK_RECEIVED:
            case TRAFFIC_RPC_REQUEST_BLOCK_METADATA:
            case TRAFFIC_RPC_COMMIT_BLOCK_SYNC:
                group = TRAFFIC_GROUP_RPC_BLOCK_OP;
                break;
            case TRAFFIC_RPC_RESPOND_BLOCK_RECIVED:
            case TRAFFIC_RPC_REQUEST_BLOCK_METADATA_RESPOND:
            case TRAFFIC_RPC_COMMIT_BLOCK_SYNC_RESPOND:
                group = TRAFFIC_GROUP_RPC_BLOCK_OP_RESPOND;
                break;
            case TRAFFIC_RPC_STARTUP_INIT:
            case TRAFFIC_RPC_ERROR:
            case TRAFFIC_RPC_UPGRATE_MAINTAIN:
            case TRAFFIC_RPC_BAD_BLOCK_REPORT:
            case TRAFFIC_RPC_GET_VERSION:
                group = TRAFFIC_GROUP_DN_RPC_OTHER;
                break;
            case TRAFFIC_RPC_RESPOND_STARTUP_INIT:
            case TRAFFIC_RPC_ERROR_RESPOND:
            case TRAFFIC_RPC_UPGRATE_MAINTAIN_RESPOND:
            case TRAFFIC_RPC_RESPOND_BAD_BLOCK_REPORT:
            case TRAFFIC_RPC_GET_VERSION_RESPOND:
                group = TRAFFIC_GROUP_DN_RPC_OTHER_RESPOND;
                break;
            case TRAFFIC_RPC_CLIENT_REQ_BLOCK_LOC:
            case TRAFFIC_RPC_CLIENT_GET_INFO_FILE:
            case TRAFFIC_RPC_CLIENT_GET_CONTENT_SUM:
                group = TRAFFIC_GROUP_RPC_CLIENT_GET_METADATA;
                break;
            case TRAFFIC_RPC_CLIENT_REQ_BLOCK_LOC_RESPOND:
            case TRAFFIC_RPC_CLIENT_GET_INFO_FILE_RESPOND:
            case TRAFFIC_RPC_CLIENT_GET_CONTENT_SUM_RESPOND:
                group = TRAFFIC_GROUP_RPC_CLIENT_GET_METADATA_RESPOND;
                break;
            case TRAFFIC_RPC_CLIENT_CREATE_FILE:
            case TRAFFIC_RPC_CLIENT_APPEND_FILE:
            case TRAFFIC_RPC_CLIENT_SET_REPLICA_FILE:
            case TRAFFIC_RPC_CLIENT_SET_PERM_FILE:
            case TRAFFIC_RPC_CLIENT_SET_OWN_FILE:
            case TRAFFIC_RPC_CLIENT_SET_TIME_FILE:
            case TRAFFIC_RPC_CLIENT_ABAND_BLK:
            case TRAFFIC_RPC_CLIENT_ADD_BLK:
            case TRAFFIC_RPC_CLIENT_COMPLETE_FILE:
            case TRAFFIC_RPC_CLIENT_REPORT_BAD_BLK:
                group = TRAFFIC_GROUP_CLIENT_RPC_FILE_OP;
                break;
            case TRAFFIC_RPC_CLIENT_CREATE_FILE_RESPOND:
            case TRAFFIC_RPC_CLIENT_APPEND_FILE_RESPOND:
            case TRAFFIC_RPC_CLIENT_SET_REPLICA_FILE_RESPOND:
            case TRAFFIC_RPC_CLIENT_SET_PERM_FILE_RESPOND:
            case TRAFFIC_RPC_CLIENT_SET_OWN_FILE_RESPOND:
            case TRAFFIC_RPC_CLIENT_SET_TIME_FILE_RESPOND:
            case TRAFFIC_RPC_CLIENT_ABAND_BLK_RESPOND:
            case TRAFFIC_RPC_CLIENT_ADD_BLK_RESPOND:
            case TRAFFIC_RPC_CLIENT_COMPLETE_FILE_RESPOND:
            case TRAFFIC_RPC_CLIENT_REPORT_BAD_BLK_RESPOND:
                group = TRAFFIC_GROUP_CLIENT_RPC_FILE_OP_RESPOND;
                break;
            case TRAFFIC_RPC_CLIENT_FSYNC:
                group = TRAFFIC_GROUP_CLIENT_RPC_FSYNC;
                break;
            case TRAFFIC_RPC_CLIENT_FSYNC_RESPOND:
                group = TRAFFIC_GROUP_CLIENT_RPC_FSYNC_RESPOND;
                break;
            case TRAFFIC_RPC_CLIENT_RENAME_PATH:
            case TRAFFIC_RPC_CLIENT_DEL_PATH:
            case TRAFFIC_RPC_CLIENT_MKDIR:
            case TRAFFIC_RPC_CLIENT_GET_DIR_LIST:
                group = TRAFFIC_GROUP_CLIENT_RPC_NAMESPACE;
                break;
            case TRAFFIC_RPC_CLIENT_RENAME_PATH_RESPOND:
            case TRAFFIC_RPC_CLIENT_DEL_PATH_RESPOND:
            case TRAFFIC_RPC_CLIENT_MKDIR_RESPOND:
            case TRAFFIC_RPC_CLIENT_GET_DIR_LIST_RESPOND:
                group = TRAFFIC_GROUP_CLIENT_RPC_NAMESPACE_RESPOND;
                break;
            case TRAFFIC_RPC_CLIENT_RENEW_LEASE:
                group = TRAFFIC_GROUP_CLIENT_RPC_LEASE;
                break;
            case TRAFFIC_RPC_CLIENT_RENEW_LEASE_RESPOND:
                group = TRAFFIC_GROUP_CLIENT_RPC_LEASE_RESPOND;
                break;
            case TRAFFIC_RPC_CLIENT_GET_STAT:
            case TRAFFIC_RPC_CLIENT_GET_DN_REPORT:
            case TRAFFIC_RPC_CLIENT_GET_PREF_BLK_SIZE:
                group = TRAFFIC_GROUP_CLIENT_RPC_STAT;
                break;
            case TRAFFIC_RPC_CLIENT_GET_STAT_RESPOND:
            case TRAFFIC_RPC_CLIENT_GET_DN_REPORT_RESPOND:
            case TRAFFIC_RPC_CLIENT_GET_PREF_BLK_SIZE_RESPOND:
                group = TRAFFIC_GROUP_CLIENT_RPC_STAT_RESPOND;
                break;
            case TRAFFIC_RPC_CLIENT_GET_TOKEN:
            case TRAFFIC_RPC_CLIENT_RENEW_TOKEN:
            case TRAFFIC_RPC_CLIENT_CANCLE_TOKEN:
                group = TRAFFIC_GROUP_CLIENT_RPC_SECURITY;
                break;
            case TRAFFIC_RPC_CLIENT_GET_TOKEN_RESPOND:
            case TRAFFIC_RPC_CLIENT_RENEW_TOKEN_RESPOND:
            case TRAFFIC_RPC_CLIENT_CANCLE_TOKEN_RESPOND:
                group = TRAFFIC_GROUP_CLIENT_RPC_SECURITY_RESPOND;
                break;
            case TRAFFIC_RPC_2ND_GET_BLOCK:
            case TRAFFIC_RPC_2ND_GET_BLOCK_KEYS:
            case TRAFFIC_RPC_2ND_GET_LOG_SIZE:
            case TRAFFIC_RPC_2ND_ROLL_LOG:
            case TRAFFIC_RPC_2ND_ROLL_FSIMG:
                group = TRAFFIC_GROUP_RPC_CHECKPOINT;
                break;
            case TRAFFIC_RPC_2ND_GET_BLOCK_RESPOND:
            case TRAFFIC_RPC_2ND_GET_BLOCK_KEYS_RESPOND:
            case TRAFFIC_RPC_2ND_GET_LOG_SIZE_RESPOND:
            case TRAFFIC_RPC_2ND_ROLL_LOG_RESPOND:
            case TRAFFIC_RPC_2ND_ROLL_FSIMG_RESPOND:
                group = TRAFFIC_GROUP_RPC_CHECKPOINT_RESPOND;
                break;
            case TRAFFIC_RPC_SECURITY_REFRESH:
            case TRAFFIC_RPC_UNKNOWN:
            case TRAFFIC_RPC_OTHER:
                group = TRAFFIC_GROUP_RPC_OTHER;
                break;
            case TRAFFIC_RPC_SECURITY_REFRESH_RESPOND:
            case TRAFFIC_RPC_UNKNOWN_RESPOND:
            case TRAFFIC_RPC_OTHER_RESPOND:
                group = TRAFFIC_GROUP_RPC_OTHER_RESPOND;
                break;
            case TRAFFIC_2ND_GET_FS_IMG:
                group = TRAFFIC_GROUP_2ND_GET_FS_IMG;
                break;
            case TRAFFIC_2ND_GET_EDIT_IMG:
                group = TRAFFIC_GROUP_2ND_GET_EDIT_IMG;
                break;
            default:
                throw new RuntimeException("Unknown Traffic Type when Grouping!!!!");
        }
        return group;
    }

    //Since now we only look at flow granurity
    //Let's do the grouping based on flow for now
    //yangsuli 12/12/2012

    public static final byte TRAFFIC_FLOW_UNKNOWN = (byte) 1;
    public static final byte TRAFFIC_FLOW_CHECKPOINT_RESPOND = (byte) 2;
    public static final byte TRAFFIC_FLOW_CHECKPOINT = (byte) 3;
    public static final byte TRAFFIC_FLOW_CLIENT_RPC_RESPOND = (byte) 4;
    public static final byte TRAFFIC_FLOW_CLIENT_RPC = (byte) 5;
    public static final byte TRAFFIC_FLOW_DN_RPC_RESPOND = (byte) 6;
    public static final byte TRAFFIC_FLOW_DN_RPC = (byte) 7;
    public static final byte TRAFFIC_FLOW_WRITE_PIPELINE = (byte) 8;
    public static final byte TRAFFIC_FLOW_WRITE_ACK = (byte) 9;
    public static final byte TRAFFIC_FLOW_READ_TRANSFER = (byte) 10;
    public static final byte TRAFFIC_FLOW_WRITE_TRANSFER = (byte) 11;
    public static final byte TRAFFIC_FLOW_READ_REQACK = (byte) 12;
    public static final byte TRAFFIC_FLOW_BLOCK_CHECKSUM = (byte) 13;
    public static final byte TRAFFIC_FLOW_DN_DATA_TRANSFER = (byte) 14;
    public static final byte TRAFFIC_FLOW_2ND_GET_FS_IMG = (byte)15;
    public static final byte TRAFFIC_FLOW_2ND_GET_EDIT_IMG = (byte)16;
    public static byte ADGFlowGroupTraffic(byte group){
        byte flow_type;
        switch(group){
            case TRAFFIC_GROUP_READ_REQACK:
                flow_type = TRAFFIC_FLOW_READ_REQACK;
                break;
            case TRAFFIC_GROUP_BLOCK_CHECKSUM:
                flow_type = TRAFFIC_FLOW_BLOCK_CHECKSUM;
                break;
            case TRAFFIC_GROUP_WRITE_TRANSFER:
                flow_type = TRAFFIC_FLOW_WRITE_TRANSFER;
                break;
            case TRAFFIC_GROUP_READ_TRANSFER:
                flow_type = TRAFFIC_FLOW_READ_TRANSFER;
                break;
            case TRAFFIC_GROUP_WRITE_ACK:
                flow_type = TRAFFIC_FLOW_WRITE_ACK;
                break;
            case TRAFFIC_GROUP_WRITE_PIPELINE:
                flow_type = TRAFFIC_FLOW_WRITE_PIPELINE;
                break;
            case TRAFFIC_GROUP_RPC_HEARTBEAT:
            case TRAFFIC_GROUP_RPC_BLOCK_REPORT:
            case TRAFFIC_GROUP_RPC_BLOCK_OP:
            case TRAFFIC_GROUP_DN_RPC_OTHER:
                flow_type = TRAFFIC_FLOW_DN_RPC;
                break;
            case TRAFFIC_GROUP_RPC_HEARTBEAT_RESPOND:
            case TRAFFIC_GROUP_RPC_BLOCK_REPORT_RESPOND:
            case TRAFFIC_GROUP_RPC_BLOCK_OP_RESPOND:
            case TRAFFIC_GROUP_DN_RPC_OTHER_RESPOND:
                flow_type = TRAFFIC_FLOW_DN_RPC_RESPOND;
                break;
            case TRAFFIC_GROUP_RPC_CLIENT_GET_METADATA:
            case TRAFFIC_GROUP_CLIENT_RPC_FILE_OP:
            case TRAFFIC_GROUP_CLIENT_RPC_FSYNC:
            case TRAFFIC_GROUP_CLIENT_RPC_NAMESPACE:
            case TRAFFIC_GROUP_CLIENT_RPC_LEASE:
            case TRAFFIC_GROUP_CLIENT_RPC_STAT:
            case TRAFFIC_GROUP_CLIENT_RPC_SECURITY:
                flow_type = TRAFFIC_FLOW_CLIENT_RPC;
                break;
            case TRAFFIC_GROUP_RPC_CLIENT_GET_METADATA_RESPOND:
            case TRAFFIC_GROUP_CLIENT_RPC_FILE_OP_RESPOND:
            case TRAFFIC_GROUP_CLIENT_RPC_FSYNC_RESPOND:
            case TRAFFIC_GROUP_CLIENT_RPC_NAMESPACE_RESPOND:
            case TRAFFIC_GROUP_CLIENT_RPC_LEASE_RESPOND:
            case TRAFFIC_GROUP_CLIENT_RPC_STAT_RESPOND:
            case TRAFFIC_GROUP_CLIENT_RPC_SECURITY_RESPOND:
                flow_type = TRAFFIC_FLOW_CLIENT_RPC_RESPOND;
                break;
            case TRAFFIC_GROUP_RPC_CHECKPOINT:
                flow_type = TRAFFIC_FLOW_CHECKPOINT;
                break;
            case TRAFFIC_GROUP_RPC_CHECKPOINT_RESPOND:
                flow_type = TRAFFIC_FLOW_CHECKPOINT_RESPOND;
                break;
            case TRAFFIC_GROUP_RPC_OTHER_RESPOND:
            case TRAFFIC_GROUP_RPC_OTHER:
                flow_type = TRAFFIC_FLOW_UNKNOWN;
                break;
      	    case TRAFFIC_GROUP_DN_DATA_TRANSFER:
		flow_type = TRAFFIC_FLOW_DN_DATA_TRANSFER;
            case TRAFFIC_GROUP_2ND_GET_FS_IMG:
        flow_type = TRAFFIC_FLOW_2ND_GET_FS_IMG;
        break;
            case TRAFFIC_GROUP_2ND_GET_EDIT_IMG:
       flow_type = TRAFFIC_FLOW_2ND_GET_EDIT_IMG;
        break;
            default:
                throw new RuntimeException("Unrecognized traffic group: " + group + "!");
        }
        return flow_type;
    }
    

    public static final byte TRAFFIC_DN_TO_CLIENT = (byte) 2;
    public static final byte TRAFFIC_DN_TO_NN = (byte) 3;
    public static final byte TRAFFIC_DN_TO_DN = (byte) 4;
    public static final byte TRAFFIC_DN_TO_DNCLIENT = (byte) 5;
    public static final byte TRAFFIC_NN_TO_DN = (byte) 6;
    public static final byte TRAFFIC_NN_TO_2NDNN = (byte) 7;
    public static final byte TRAFFIC_NN_TO_CLIENT = (byte) 8;
    public static final byte TRAFFIC_CLIENT_TO_DN = (byte) 1;
    public static final byte TRAFFIC_CLIENT_TO_NN = (byte) 9;
    public static final byte TRAFFIC_UNKNOWN = (byte) 10;
    public static final byte TRAFFIC_2NDNN_TO_NN = (byte) 11;
    
    public static byte ADGFlowLocGroupTraffic(byte group){
        byte loc_type;
        switch(group){
            case TRAFFIC_GROUP_READ_REQACK:
            case TRAFFIC_GROUP_WRITE_TRANSFER:
            case TRAFFIC_GROUP_BLOCK_CHECKSUM:
                loc_type = TRAFFIC_CLIENT_TO_DN;
                break;
            case TRAFFIC_GROUP_READ_TRANSFER:
                loc_type = TRAFFIC_DN_TO_CLIENT;
                break;
            case TRAFFIC_GROUP_WRITE_ACK:
                loc_type = TRAFFIC_DN_TO_DNCLIENT;
                break;
            case TRAFFIC_GROUP_WRITE_PIPELINE:
                loc_type = TRAFFIC_DN_TO_DN;
                break;
            case TRAFFIC_GROUP_RPC_HEARTBEAT:
            case TRAFFIC_GROUP_RPC_BLOCK_REPORT:
            case TRAFFIC_GROUP_RPC_BLOCK_OP:
            case TRAFFIC_GROUP_DN_RPC_OTHER:
                loc_type = TRAFFIC_DN_TO_NN;
                break;
            case TRAFFIC_GROUP_RPC_HEARTBEAT_RESPOND:
            case TRAFFIC_GROUP_RPC_BLOCK_REPORT_RESPOND:
            case TRAFFIC_GROUP_RPC_BLOCK_OP_RESPOND:
            case TRAFFIC_GROUP_DN_RPC_OTHER_RESPOND:
                loc_type = TRAFFIC_NN_TO_DN;
                break;
            case TRAFFIC_GROUP_RPC_CLIENT_GET_METADATA:
            case TRAFFIC_GROUP_CLIENT_RPC_FILE_OP:
            case TRAFFIC_GROUP_CLIENT_RPC_FSYNC:
            case TRAFFIC_GROUP_CLIENT_RPC_NAMESPACE:
            case TRAFFIC_GROUP_CLIENT_RPC_LEASE:
            case TRAFFIC_GROUP_CLIENT_RPC_STAT:
            case TRAFFIC_GROUP_CLIENT_RPC_SECURITY:
                loc_type = TRAFFIC_CLIENT_TO_NN;
                break;
            case TRAFFIC_GROUP_RPC_CLIENT_GET_METADATA_RESPOND:
            case TRAFFIC_GROUP_CLIENT_RPC_FILE_OP_RESPOND:
            case TRAFFIC_GROUP_CLIENT_RPC_FSYNC_RESPOND:
            case TRAFFIC_GROUP_CLIENT_RPC_NAMESPACE_RESPOND:
            case TRAFFIC_GROUP_CLIENT_RPC_LEASE_RESPOND:
            case TRAFFIC_GROUP_CLIENT_RPC_STAT_RESPOND:
            case TRAFFIC_GROUP_CLIENT_RPC_SECURITY_RESPOND:
                loc_type = TRAFFIC_NN_TO_CLIENT;
                break;
            case TRAFFIC_GROUP_RPC_CHECKPOINT:
                loc_type = TRAFFIC_2NDNN_TO_NN;
                break;
            case TRAFFIC_GROUP_RPC_CHECKPOINT_RESPOND:
            //FIXME:
            //honestly I am not quite sure whether get img always from nn to 2nd nn
            //Needs to confirm
            //yangsuli 12/21/2012
            case TRAFFIC_GROUP_2ND_GET_FS_IMG:
            case TRAFFIC_GROUP_2ND_GET_EDIT_IMG:
                loc_type = TRAFFIC_NN_TO_2NDNN;
                break;
            case TRAFFIC_GROUP_RPC_OTHER_RESPOND:
            case TRAFFIC_GROUP_RPC_OTHER:
                loc_type = TRAFFIC_UNKNOWN;
                break;
            default:
                throw new RuntimeException("Unrecognized traffic group!");
        }
        return loc_type;
    }


    

            



    //ADG
    //For the RPC style communication
    //internally, it should aslo call ADGSetSocketTrafficType(sock, desc).
    //Just needs to determine traffic type according to method name
    //SO it is easer for whoever calls it.
    //yangsuli 11/30/2012
    public static boolean ADGSetRPCSendTrafficType(Socket sock, String method, String tag){
        //LOG.info("ADG RPC Request Method (Tag: " + tag + "): "+method);        
        if(method.equals("sendHeartbeat")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_SEND_HEARTBEAT),tag);  
        }else if(method.equals("register") ||
                method.equals("getBuildVersion")
                ){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_STARTUP_INIT),tag);  
        }else if(method.equals("blockReport")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_BLOCK_REPORT), tag);
        }else if(method.equals("blockReceived")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_SEND_BLOCK_RECEIVED), tag);
        }else if(method.equals("errorReport") ||
                method.equals("reportBadBlocks")
                ){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_ERROR), tag);
        }else if(method.equals("processUpgradeCommand") ||
                method.equals("setSafeMode") ||
                method.equals("saveNamespace") ||
                method.equals("refreshNodes") ||
                method.equals("finalizeUpgrade") ||
                method.equals("distributedUpgradeProgress") ||
                method.equals("metaSave") ||
                method.equals("setQuota")
                ){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_UPGRATE_MAINTAIN), tag);
        }else if(method.equals("nextGenerationStamp")
                ){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_REQUEST_BLOCK_METADATA), tag);
        }else if(method.equals("commitBlockSynchronization")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_COMMIT_BLOCK_SYNC), tag); 
        }else if(method.equals("getProtocolVersion") ||
                method.equals("versionRequest") 
                ){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_GET_VERSION), tag);
        }else if(method.equals("getBlockLocations")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_REQ_BLOCK_LOC), tag);
        }else if(method.equals("create")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_CREATE_FILE), tag);
        }else if(method.equals("append")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_APPEND_FILE), tag);
        }else if(method.equals("setReplication")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_SET_REPLICA_FILE), tag);
        }else if(method.equals("setPermission")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_SET_PERM_FILE), tag);
        }else if(method.equals("setOwner")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_SET_OWN_FILE), tag);
        }else if(method.equals("abandonBlock")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_ABAND_BLK), tag);
        }else if(method.equals("addBlock")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_ADD_BLK), tag);
        }else if(method.equals("complete")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_COMPLETE_FILE), tag);
        }else if(method.equals("getFileInfo")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_INFO_FILE), tag);
        }else if(method.equals("getContentSummary")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_CONTENT_SUM), tag);
        }else if(method.equals("fsync")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_FSYNC), tag);
        }else if(method.equals("rename")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_RENAME_PATH), tag);
        }else if(method.equals("delete")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_DEL_PATH), tag);
        }else if(method.equals("mkdirs")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_MKDIR), tag);
        }else if(method.equals("getListing")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_DIR_LIST), tag);
        }else if(method.equals("renewLease")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_RENEW_LEASE), tag);
        }else if(method.equals("getStats")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_STAT), tag);
        }else if(method.equals("getDataNodeInfo")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_DN_REPORT), tag);
        }else if(method.equals("getPreferredBlockSize")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_PREF_BLK_SIZE), tag);
        }else if(method.equals("setTimes")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_SET_TIME_FILE), tag);
        }else if(method.equals("getDelegationToken")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_TOKEN), tag);
        }else if(method.equals("renewDelegationToken")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_RENEW_TOKEN), tag);
        }else if(method.equals("cancleDelegationToken")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_CANCLE_TOKEN), tag);
        }else if(method.equals("getBlocks")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_2ND_GET_BLOCK), tag);
        }else if(method.equals("getBlockKeys")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_2ND_GET_BLOCK_KEYS), tag);
        }else if(method.equals("getEditLogSize")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_2ND_GET_LOG_SIZE), tag);
        }else if(method.equals("rollEditLog")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_2ND_ROLL_LOG), tag);
        }else if(method.equals("rollFsImage")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_2ND_ROLL_FSIMG), tag);
        }else if(method.equals("refreshUserToGroupsMappings") ||
                method.equals("refreshSuperUserGroupsConfiguration") ||
                method.equals("refreshServiceAcl")
                ){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_SECURITY_REFRESH), tag);
        }else if(method.equals("heartbeat") ||
                method.equals("getSystemDir")
                ){//Somt non-HDFS traffic, really shouldn't be here
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_OTHER), tag);
        }else{
            LOG.warn("ADG WARNING:Don't know such a method: " + method +"! Probably missed something!!!!");
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_UNKNOWN), tag);
        }

    }


    public static boolean ADGSetRPCResponseTrafficType(Socket sock, String method, String tag){
  //      LOG.info("ADG RPC Respond Method (Tag: " + tag + "): "+method);        
        if(method.equals("sendHeartbeat")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_RESPOND_HEARTBEAT),tag);  
        }else if(method.equals("register")
                ){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_RESPOND_STARTUP_INIT),tag);  
        }else if(method.equals("blockReport")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_RESPOND_BLOCK_REPORT), tag);
        }else if(method.equals("blockReceived")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_RESPOND_BLOCK_RECIVED), tag);
        }else if(method.equals("errorReport") ||
                method.equals("reportBadBlocks")
                ){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_ERROR_RESPOND), tag);
        }else if(method.equals("processUpgradeCommand") ||
                method.equals("setSafeMode") ||
                method.equals("saveNamespace") ||
                method.equals("refreshNodes") ||
                method.equals("finalizeUpgrade") ||
                method.equals("distributedUpgradeProgress") ||
                method.equals("metaSave") ||
                method.equals("setQuota")

                ){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_UPGRATE_MAINTAIN_RESPOND), tag);
        }else if(method.equals("nextGenerationStamp")
                ){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_REQUEST_BLOCK_METADATA_RESPOND), tag);
        }else if(method.equals("commitBlockSynchronization")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_COMMIT_BLOCK_SYNC_RESPOND), tag); 
        }else if(method.equals("getProtocolVersion") ||
                method.equals("versionRequest") 
                ){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_GET_VERSION_RESPOND), tag);
        }else if(method.equals("getBlockLocations")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_REQ_BLOCK_LOC_RESPOND), tag);
        }else if(method.equals("create")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_CREATE_FILE_RESPOND), tag);
        }else if(method.equals("append")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_APPEND_FILE_RESPOND), tag);
        }else if(method.equals("setReplication")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_SET_REPLICA_FILE_RESPOND), tag);
        }else if(method.equals("setPermission")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_SET_PERM_FILE_RESPOND), tag);
        }else if(method.equals("setOwner")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_SET_OWN_FILE_RESPOND), tag);
        }else if(method.equals("abandonBlock")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_ABAND_BLK_RESPOND), tag);
        }else if(method.equals("addBlock")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_ADD_BLK_RESPOND), tag);
        }else if(method.equals("complete")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_COMPLETE_FILE_RESPOND), tag);
        }else if(method.equals("getFileInfo")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_INFO_FILE_RESPOND), tag);
        }else if(method.equals("getContentSummary")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_CONTENT_SUM_RESPOND), tag);
        }else if(method.equals("fsync")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_FSYNC_RESPOND), tag);
        }else if(method.equals("rename")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_RENAME_PATH_RESPOND), tag);
        }else if(method.equals("delete")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_DEL_PATH_RESPOND), tag);
        }else if(method.equals("mkdirs")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_MKDIR_RESPOND), tag);
        }else if(method.equals("getListing")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_DIR_LIST_RESPOND), tag);
        }else if(method.equals("renewLease")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_RENEW_LEASE_RESPOND), tag);
        }else if(method.equals("getStats")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_STAT_RESPOND), tag);
        }else if(method.equals("getDataNodeInfo")){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_DN_REPORT_RESPOND), tag);
        }else if(method.equals("getPreferredBlockSize")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_PREF_BLK_SIZE_RESPOND), tag);
        }else if(method.equals("setTimes")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_SET_TIME_FILE_RESPOND), tag);
        }else if(method.equals("getDelegationToken")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_GET_TOKEN_RESPOND), tag);
        }else if(method.equals("renewDelegationToken")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_RENEW_TOKEN_RESPOND), tag);
        }else if(method.equals("cancleDelegationToken")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_CLIENT_CANCLE_TOKEN_RESPOND), tag);
        }else if(method.equals("getBlocks")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_2ND_GET_BLOCK_RESPOND), tag);
        }else if(method.equals("getBlockKeys")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_2ND_GET_BLOCK_KEYS_RESPOND), tag);
        }else if(method.equals("getEditLogSize")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_2ND_GET_LOG_SIZE_RESPOND), tag);
        }else if(method.equals("rollEditLog")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_2ND_ROLL_LOG_RESPOND), tag);
        }else if(method.equals("rollFsImage")){ 
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_2ND_ROLL_FSIMG_RESPOND), tag);
        }else if(method.equals("refreshUserToGroupsMappings") ||
                method.equals("refreshSuperUserGroupsConfiguration") ||
                method.equals("refreshServiceAcl")
                ){
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_SECURITY_REFRESH_RESPOND), tag);
        }else if(method.equals("heartbeat")){//Somt non-HDFS traffic, really shouldn't be here
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_OTHER_RESPOND), tag);
        }else{
            LOG.warn("ADG WARNING:Don't know such a method! Probably missed something!!!!");
            return ADGSetSocketTrafficType(sock, new ADGTrafficDesc(TRAFFIC_RPC_UNKNOWN_RESPOND), tag);
        }


    }



    public static boolean ADGSetSocketTrafficType(Socket sock, ADGTrafficDesc desc){

        return ADGSetSocketTrafficType(sock, desc, "DefaultSetTrafficTAG");
        //ADG TODO:
        //call into the ioctl of sock, set the appropriate field
        //TODO:
        //invoke ioctl system call on sock
        //in tcp_ioctl function implementation
        //you need to add a SET_SOCKET_TYPE command
        //and inside that command, what you need to do is to set a field in the struct sock
        //then you need to modify tcp_transmit_skb (tcp layer) or ip_queue_xmit (ip layer) in their header construting process
        //You need consult the correspoinding sock struct's field, 
        //yangsuli 11/25/2012
    }

    public static boolean ADGSetSocketTrafficType(Socket sock, ADGTrafficDesc desc, String tag){
        int flow_type = ADGFlowGroupTraffic(ADGGroupTraffic(desc.type));
    //    LOG.info("ADG Set Flow Type: " + flow_type);        
        if(sock == null){
            LOG.error("Null socket when setting traffic type for flow type" + flow_type + "!");
            return false;
        }

     //  LOG.info("YANGSULI socket's class is:" + sock.getClass().getName());
        int ret = ADGSetSocketTOS.setSocketTOS((sun.nio.ch.SocketAdaptor)sock, flow_type);
        if(ret != 0){
            LOG.error("Error when etting flow type " + flow_type +  "!");
            return false;
        }
        
        return true;
    }

    public static boolean ADGSetServerSocketTrafficType(ServerSocket sock, ADGTrafficDesc desc, String tag){

        int flow_type = ADGFlowGroupTraffic(ADGGroupTraffic(desc.type));
    //    LOG.info("ADG Set Flow Type: " + flow_type);        
        if(sock == null){
            LOG.error("Null socket when setting traffic type for flow type" + flow_type + "!");
            return false;
        }

     //  LOG.info("YANGSULI socket's class is:" + sock.getClass().getName());
        int ret = ADGSetSocketTOS.setServerSocketTOS(sock, flow_type);
        if(ret != 0){
            LOG.error("Error when etting flow type " + flow_type +  "!");
            return false;
        }
        
        return true;
    }
}
