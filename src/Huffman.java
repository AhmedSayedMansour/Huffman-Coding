import java.util.*;

public class Huffman implements Comparator<node> {
    public static HashMap<Character,Integer> map = new HashMap<Character,Integer>();

    public static void setCode(node root){
        if(root == null){
            return;
        }
        if(root.parent == null){
            root.left.code = "1";
            if (root.right!=null){root.right.code = "0";}
            setCode(root.left);
            setCode(root.right);
        }
        else{
            if(root.left!=null){
                root.left.code = root.code + "1";
                setCode(root.left);
            }
            if(root.right!=null){
                root.right.code = root.code + "0";
                setCode(root.right);
            }
        }
    }

    public static void calcSeq(String s){
        for(int i = 0; i < s.length(); i++){
            Character c = s.charAt(i);

            if(map.containsKey(c)){
                map.put(c,map.get(c)+1);
            }else{
                map.put(c,1);
            }
        }
    }

    @Override
    public int compare(node o1, node o2) {
        return o1.prob.compareTo(o2.prob);
    }

    public static String code;
    public static void getCode(node root,String c){
        if(root == null){
            return;
        }
        if (root.data.equals(c)){
            code = root.code;
        }
        else{
            getCode(root.left, c);
            getCode(root.right, c);
        }
    }

    public static String Compress (String s){
        node root = new node();
        ArrayList<node> arr = new ArrayList<>();
        String output="";
        calcSeq(s);
        for ( Map.Entry<Character,Integer> entry : map.entrySet()) {
            Character key = entry.getKey();
            Integer val = entry.getValue();
            node n = new node();
            n.data = ""+ key;
            n.prob = val;
            arr.add(n);
        }
        //building nodes
        while(arr.size()>2){
            Collections.sort(arr , new Huffman());
            node n = new node();
            n.left = arr.get(0);
            n.right = arr.get(1);
            n.prob = n.left.prob= + n.right.prob;
            n.left.parent = n;
            n.right.parent = n;
            arr.remove(0);
            arr.remove(0);
            arr.add(n);
        }
        //building tree
        if(arr.size()==2){
            root.left = arr.get(0);
            root.right = arr.get(1);
            root.left.parent = root;
            root.right.parent = root;
            arr.remove(0);
            arr.remove(0);
        }else{          //if one character
            root.left = arr.get(0);
            root.left.parent = root;
            arr.remove(0);
        }
        setCode(root);
        for(int i=0;i<s.length();i++) {
            char c = s.charAt(i);
            getCode(root, "" + c);
            output += code;
        }
        return output;
    }

    public static String Decompress(String c){
        ArrayList<node> arr = new ArrayList<>();
        String output = "";
        for ( Map.Entry<Character,Integer> entry : map.entrySet()) {
            Character key = entry.getKey();
            Integer val = entry.getValue();
            node n = new node();
            n.data = ""+ key;
            n.prob = val;
            arr.add(n);
        }
        while(arr.size()>2){
            Collections.sort(arr , new Huffman());
            node n = new node();
            n.left = arr.get(0);
            n.right = arr.get(1);
            n.prob = n.left.prob + n.right.prob;
            n.left.parent = n;
            n.right.parent = n;
            arr.remove(0);
            arr.remove(0);
            arr.add(n);
        }
        node root = new node();
        if(arr.size()==2){
            root.left = arr.get(0);
            root.right = arr.get(1);
            root.left.parent = root;
            root.right.parent = root;
            arr.remove(0);
            arr.remove(0);
        }
        else{
            root.left = arr.get(0);
            root.left.parent = root;
            arr.remove(0);
        }
        setCode(root);
        for(int i=0;i<c.length();i++){
            String Cw = ""+c.charAt(i);
            node n = (root.left.code.equals(Cw)) ? root.left : root.right;
            while(n.left !=null || n.right !=null){
                i++;
                Cw += c.charAt(i);
                n = (n.left.code.equals(Cw)) ? n.left : n.right;
            }
            output+=n.data;
        }
        return output;
    }

    public static void main(String[] args) {
        System.out.println("Enter text to compress");
        Scanner in = new Scanner(System.in);
        String S = in.nextLine();
        System.out.println(Compress(S));
        System.out.println("Enter code to decompress");
        S = in.nextLine();
        System.out.println(Decompress(S));
    }
}
