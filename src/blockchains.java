import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class blockchains {

    //区块
    public class Block {
        public int index;
        public String hash;
        public String previousHash;
        public String data;
        public String merkleRoot;
        public List<Transaction> transactions ; //our data will be a simple message.
        public long timeStamp; //as number of milliseconds since 1/1/1970.
        public int nonce;

        //Block Constructor.
        public Block(String hash,String data,String previousHash ) {
            this.hash = hash;
            this.data = data;
            this.previousHash = previousHash;
        }

        public Block(String data, String previousHash) {
            this.data = data;
            this.previousHash = previousHash;
            this.timeStamp = new Date().getTime();
            this.hash = calculateHash();
        }

    /*
    调用数字签名方法 计算当前区块hash值
     */

        public String calculateHash() {
            String calculatedhash = StringUtil.applySha256(
                    previousHash + Long.toString(timeStamp) +
                            Integer.toString(nonce) + data);
            return calculatedhash;
        }

    /*
    工作量证明（difficulty表示hash的前几位全部为0）
     */

        public void mineBlock(int difficulty) {
            String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
            while(!hash.substring( 0, difficulty).equals(target)) {
                nonce ++;
                hash = calculateHash();
            }
            System.out.println("Block Mined!!! : " + hash);
            System.out.println("随机数："+ nonce);
        }

        public int getIndex(){
            return this.index;
        }
        public String getPreHash(){
            return this.previousHash;
        }
        public long getTimeStamp(){
            return this.timeStamp;
        }
        public List<Transaction> getTransactions(){
            return this.transactions;
        }
        public int getNonce(){
            return this.nonce;
        }
        public String getHash(){
            return this.hash;
        }
        public String Output(){
            String s = "\n\n" + "Index:\t" + Integer.toString(this.index) + '\n'   //用toString转换成字符穿
                    + "PreHash:\t" + this.previousHash + '\n' + "TimeStamp:\t"
                    + Long.toString(this.timeStamp) + '\n' + "Data:\n" ;
            if(this.transactions != null){
                for(Transaction i : this.transactions){
                    s = s + "\t\t" + i.toString() + '\n';
                }
            }
            else{
                s = s + "\t\tnull!\n";
            }
            s = s + "Nonce:\t\t" + Integer.toString(this.nonce) + '\n'
                    + "Hash:\t\t" + this.hash + "\n\n";
            return s;
        }
    }

    //账户
    public static class Wallet{
        public String name;
        public PublicKey sender; //交易发送方/公钥
        public PublicKey reciepient; //交易接收方/私钥
        public float value; //交易金额
        public double balance;
        public Wallet(String na){
            try{
                this.name=na;
                this.balance=0;
                String ha=calulateHash();     //计算输出的hash值
            }
            catch (Exception e){
                System.out.println(e);
            }
        }

        public void sendBalance(List<Block> bk){
            double b1=0;
            for(Block block:bk){
                if(block.getTransactions()==null) continue;
                for(Transaction ta:block.getTransactions()){
                    if(ta.getFromUser()==this) b1=b1-ta.getAmount();
                    if(ta.getToUser()==this) b1=b1+ta.getAmount();
                }
            }
            if(Math.abs(this.balance-b1) < 0.0000001) System.out.println("余额不须要改变！(Balance unchanged!)");
            else{
                this.balance = b1;
                System.out.println("余额已刷新！(Balance refreshed!)");
            }
        }

        public PublicKey getPublicKey(){
            return this.sender;
        }
        public String getName(){
            return this.name;
        }

        //计算hash值
        private String calulateHash() {
            balance++;
            return StringUtil.applySha256(
                    StringUtil.getStringFromKey(sender) +
                            StringUtil.getStringFromKey(reciepient) +
                            Float.toString(value) + balance
            );
        }
    }

    //校验交易
    public boolean createTransaction(Wallet res,double amount){
        if(amount > res.balance) return false; //不够钱
        if(amount <= 0.0) return false;
        boolean result=false;

        return false;
    }

    //交易
    public static class Transaction{
        private Wallet fromUser;  //转账方
        private Wallet toUser;    //接收方
        private double amount;     //转账数目
        private String data = "";  //附加信息
        public Transaction(Wallet f,Wallet t,double money,String dt){
            this.fromUser = f;
            this.toUser = t;
            this.amount = money;
            this.data = dt;
        }
        public Wallet getFromUser(){
            return this.fromUser;
        }
        public Wallet getToUser(){
            return this.toUser;
        }
        public double getAmount(){
            return this.amount;
        }
        public String getData(){
            return this.data;
        }
        public String Output(){
            return "from: " + this.fromUser.getName() + " , to: " + this.toUser.getName() + " , amount: " + this.amount + " , data: \"" + this.data + "\"";
        }
    }

    //创建账户
    public static boolean createAccount(String name,String passwd){  //创建（注册）账户，输入账户名和账户密码
        if(accounts.get(name) != null){
            System.out.println("账户 " + name + " 已存在！(Account " + name + " existed!)");
            return false;
        }
        Wallet acc = new Wallet(name);
        accounts.put(name,acc);
        return true;
    }

    static Map<String,Wallet> accounts = new HashMap<String,Wallet>();  //存账户



}
