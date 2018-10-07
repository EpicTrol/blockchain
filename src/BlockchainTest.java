/*
区块测试
 */

public class BlockchainTest {
    public static void main(String[] args){
        //first block
        Block firstBlock=new Block("first","0");
        System.out.println("block 1 Hash:"+firstBlock.hash);

        //second block
        Block secondBlock=new Block("second",firstBlock.hash);
        System.out.println("block 1 Hash:"+secondBlock.hash);

        //third block
        Block thirdBlock=new Block("third",secondBlock.hash);
        System.out.println("block 1 Hash:"+thirdBlock.hash);
    }
}
