package mpp.Interface;

public interface IRepoCuvinte extends IRepository<Integer, String>{
    public String[] find5Pairs();
}
