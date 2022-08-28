package me.jalar.ApplicationLayerProtocol.FTP;

import java.util.*;
import java.io.*;
import java.net.*;

public class SendProject{
    public static void sendFile(String path,String cloudPath,String destAdd,int destPort)throws Exception{

        System.out.println(path+" sending file -> "+cloudPath);
        
        File file=new File(path);
        Scanner fin=new Scanner(file);


        Socket server=new Socket(destAdd,destPort);
        InputStream nin=server.getInputStream();
        OutputStream nout=server.getOutputStream();
        Scanner ip=new Scanner(nin);
        PrintStream op=new PrintStream(nout);

        //foregin path
        System.out.println("cloud "+ip.nextLine());
        op.println(cloudPath);


        //length
        
        System.out.println(ip.nextLine());
        op.println(file.length());
        //enter the body;
        while(fin.hasNextLine()){
            op.println(fin.nextLine());
        }

        op.flush();

        String msg=ip.nextLine();

        if(msg.equals("Done")){
            System.out.println("file written");
        }else System.out.println("file is not written");



        

    }


    public static void main(String[] args)throws Exception{
        System.out.println("project directory");
        Scanner ip=new Scanner(System.in);
        String folderPath=ip.nextLine();

        System.out.println("dest ipaddress");
        String address=ip.nextLine();

        System.out.println("dest port");
        int port=ip.nextInt();
        ip.nextLine();

        System.out.println("cloud project path");
        String cloudPath=ip.nextLine();

        sendFolder(folderPath,cloudPath,address,port);
    }

    public static void sendFolder(String localPath,String cloudPath,String destAdd,int destPort)throws Exception{
        System.out.println(localPath+"  sending folder -> "+cloudPath);
        Socket server=new Socket(destAdd,destPort);
        InputStream nin=server.getInputStream();
        OutputStream nout=server.getOutputStream();
        Scanner ip=new Scanner(nin);
        PrintStream op=new PrintStream(nout);
        System.out.println(ip.nextLine());
        op.println(cloudPath+" -f");
        if(localPath.charAt(localPath.length()-1)!='/'){
            localPath+='/';
        }
        if(cloudPath.charAt(cloudPath.length()-1)!='/')cloudPath+='/';


        File folder=new File(localPath);
        String[] files=folder.list();
        for(int i=0;i<files.length;i++){
            File temp=new File(localPath+files[i]);
            if(temp.isDirectory()){
                sendFolder(localPath+files[i],cloudPath+files[i],destAdd,destPort);
            }else{
                sendFile(localPath+files[i],cloudPath+files[i],destAdd,destPort);
            }
        }

    }

    
}