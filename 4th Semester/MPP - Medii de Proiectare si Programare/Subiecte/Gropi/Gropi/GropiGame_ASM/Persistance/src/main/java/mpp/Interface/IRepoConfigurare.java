package mpp.Interface;

public interface IRepoConfigurare extends IRepository<Integer, mpp.Configurare>{
    public void updateWithId(Integer id, String attribute, String newValue);
}
