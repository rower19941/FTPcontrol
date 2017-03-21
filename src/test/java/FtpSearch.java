import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

import java.io.IOException;

/**
 * Created by hunte on 21.03.2017.
 */

public class FtpSearch {

    public static void main(String[] args) throws IOException {
        String server = "localhost";
        int port = 21;
        String user = "test";
        String pass = "";

        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);

            ftpClient.login(user, pass);

            ftpClient.enterLocalPassiveMode();

            String dirToSearch = "";

            FTPFileFilter filter = new FTPFileFilter() {

                @Override
                public boolean accept(FTPFile ftpFile) {

                    return (ftpFile.isFile() && ftpFile.getName().contains("pdf"));
                }
            };

            FTPFile[] result = ftpClient.listFiles(dirToSearch, filter);

            if (result != null && result.length > 0) {
                System.out.println("SEARCH RESULT:");
                for (FTPFile aFile : result) {
                    System.out.println(aFile.getName());
                }
            }

            ftpClient.logout();

            ftpClient.disconnect();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
