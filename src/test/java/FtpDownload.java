import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by hunte on 21.03.2017.
 */
public class FtpDownload {

    public static void main(String[] args) {
        try {
            final String localFolder = "C:/FtpFiles";
            final String ftpFolder = "/Preview";
            final String ftpName = "62.109.16.199";
            final String ftpUserName = "ftp";
            final String ftpUserPassword = "";
           // final int port = 21;
            Thread run = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                            newFtpClient(localFolder, ftpFolder, ftpName, ftpUserName, ftpUserPassword/*,port*/);
                            Thread.sleep(15000);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            run.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void newFtpClient(String localFolder,
                                     String ftpFolder,
                                     String ftpName,
                                     String ftpUserName,
                                     String ftpUserPassword) throws IOException {
        FTPClient ftp = new FTPClient();
        //try to connect
        ftp.connect(ftpName);
        //login to server
        if (!ftp.login(ftpUserName, ftpUserPassword)) {
            ftp.logout();
        }
        int reply = ftp.getReplyCode();
        //FTPReply stores a set of constants for FTP reply codes.
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
        }

        //enter passive mode
        ftp.enterLocalPassiveMode();
        //get system name
        System.out.println("Remote system is " + ftp.getSystemType());
        //change current directory
        ftp.changeWorkingDirectory("srv");
        System.out.println("Current directory is " + ftp.printWorkingDirectory());

        //get list of filenames
        FTPFile[] ftpFiles = ftp.listFiles();

        File localFilesFolder = new File(localFolder);
        File[] listOFLocalFiles = localFilesFolder.listFiles();
        int counter = 0;
        long testSize = 0;
        if (ftpFiles != null && ftpFiles.length > 0) {
            for (FTPFile ftpFile : ftpFiles) {
                for (File localFile : listOFLocalFiles) {
                    testSize = localFile.length();
                    if(ftpFile.getName().equals(localFile.getName())){
                       if(ftpFile.getSize() == localFile.length())
                       {
                           System.out.println("local has file:" + localFile.getName()+
                                   " localSize:"+localFile.length()+
                                   " ftpSize" + ftpFile.getSize()
                           );

                           counter++;
                           break;
                       }


                    }

                }
                if(counter == 1 ){
                    counter = 0;
                }
                else {
                    System.out.println("Downloading  File is " + ftpFile.getName());
                    //get output stream
                    OutputStream output;
                    output = new FileOutputStream(localFolder + "/" + ftpFile.getName());
                    //get the ftpFile from the remote system
                    ftp.retrieveFile(ftpFile.getName(), output);
                    //close output stream
                    output.close();
                    continue;
                }
            }
        }
        ftp.logout();
        ftp.disconnect();
        System.out.println("----------------------------------------");
    }
}
