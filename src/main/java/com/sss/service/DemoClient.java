package com.sss.service;

import com.sss.client.NewRpcClient;


/**
 * DemoClient class
 *
 * @author Sss
 * @date 2018/12/28
 */
public class DemoClient {
    private NewRpcClient rpcClient;

    public DemoClient(NewRpcClient newRpcClient){
        this.rpcClient = newRpcClient;
        this.rpcClient.register("fib_res",Long.class).register("exp_res",ExpOutput.class);
    }

    public long fib(int n){
        return (Long) rpcClient.send("fib_req",n);
    }

    public ExpOutput exp(int base,int exp){
        return (ExpOutput) rpcClient.send("exp_req",new ExpInput(base,exp));
    }

    public static void main(String[] args) {
        NewRpcClient rpcClient = new NewRpcClient("localhost",8888);
        DemoClient client = new DemoClient(rpcClient);

        for (int i = 0; i < 10 ; i++) {

            try{
                System.out.printf("fib(%d) = %d\n",i,client.fib(i));
                Thread.sleep(100);
            }catch(Exception e){
                i--;
            }

        }

        for (int i = 0; i < 30; i++) {
            try {
                ExpOutput res = client.exp(2, i);
                Thread.sleep(100);
                System.out.printf("exp2(%d) = %d cost=%dns\n", i, res.getValue(), res.getCostInNanos());
            } catch (Exception e) {
                i--; // retry
            }
        }

        rpcClient.close();
    }
}
