package com.lite.blackdream.business.service;

import com.lite.blackdream.business.domain.*;
import com.lite.blackdream.business.parameter.datamodel.*;
import com.lite.blackdream.business.repository.*;
import com.lite.blackdream.framework.exception.AppException;
import com.lite.blackdream.framework.component.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author LaineyC
 */
@Service
public class DataModelServiceImpl extends BaseService implements DataModelService{

    @Autowired
    private DataModelRepository dataModelRepository;

    @Autowired
    private DynamicModelRepository dynamicModelRepository;

    @Autowired
    private GeneratorInstanceRepository generatorInstanceRepository;

    @Autowired
    private GeneratorRepository generatorRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DataModel create(DataModelCreateRequest request) {
        Long userId = request.getAuthentication().getUserId();

        Long rootId = request.getRootId();
        DataModel rootPersistence = dataModelRepository.selectById(rootId);
        if(rootPersistence == null){
            throw new AppException("root不存在");
        }

        GeneratorInstance generatorInstancePersistence = generatorInstanceRepository.selectById(rootPersistence.getGeneratorInstance().getId());
        if(generatorInstancePersistence == null){
            throw new AppException("实例不存在");
        }

        Long parentId = request.getParentId();
        DataModel parentPersistence = dataModelRepository.selectById(parentId, rootPersistence);
        if(parentPersistence == null){
            throw new AppException("parent不存在");
        }

        Long dynamicModelId = request.getDynamicModelId();
        DynamicModel dynamicModelPersistence = dynamicModelRepository.selectById(dynamicModelId);
        if(dynamicModelPersistence == null){
            throw new AppException("数据模型不存在");
        }

        Generator generatorPersistence = generatorRepository.selectById(rootPersistence.getGenerator().getId());
        if(generatorInstancePersistence.getVersion() < generatorPersistence.getVersion()){
            throw new AppException("当前生成器已升级发布，请刷新数据，重新操作！");
        }

        if(!generatorPersistence.getIsOpen() && !generatorPersistence.getDeveloper().getId().equals(userId)){
            throw new AppException("当前生成器正在维护，请暂停操作，等待发布！");
        }

        DataModel dataModel = new DataModel();
        dataModel.setId(idWorker.nextId());
        dataModel.setName(request.getName());
        dataModel.setIsDelete(false);
        dataModel.setCreateDate(new Date());
        dataModel.setModifyDate(new Date());
        dataModel.setSequence(Integer.MAX_VALUE);
        dataModel.setIsExpand(request.getIsExpand());
        User user = new User();
        user.setId(userId);
        dataModel.setUser(user);
        dataModel.setDynamicModel(dynamicModelPersistence);
        dataModel.setGeneratorInstance(rootPersistence.getGeneratorInstance());
        dataModel.setGenerator(rootPersistence.getGenerator());
        DataModel parent = new DataModel();
        parent.setId(parentPersistence.getId());
        dataModel.setParent(parent);
        dataModel.setProperties(request.getProperties());
        dataModel.setAssociation(request.getAssociation());
        dataModelRepository.insert(dataModel, rootPersistence);

        generatorInstancePersistence.setModifyDate(new Date());
        generatorInstanceRepository.update(generatorInstancePersistence);

        return dataModel;
    }

    @Override
    public DataModel delete(DataModelDeleteRequest request) {
        Long id = request.getId();
        Long userId = request.getAuthentication().getUserId();

        Long rootId = request.getRootId();
        DataModel rootPersistence = dataModelRepository.selectById(rootId);
        if(rootPersistence == null){
            throw new AppException("root不存在");
        }

        if(!userId.equals(rootPersistence.getUser().getId())){
            throw new AppException("权限不足");
        }

        GeneratorInstance generatorInstancePersistence = generatorInstanceRepository.selectById(rootPersistence.getGeneratorInstance().getId());
        if(generatorInstancePersistence == null){
            throw new AppException("实例不存在");
        }

        DataModel dataModelPersistence = dataModelRepository.selectById(id, rootPersistence);
        if(dataModelPersistence == null) {
            throw new AppException("数据不存在");
        }

        Generator generatorPersistence = generatorRepository.selectById(dataModelPersistence.getGenerator().getId());
        if(generatorInstancePersistence.getVersion() < generatorPersistence.getVersion()){
            throw new AppException("当前生成器已升级发布，请刷新数据，重新操作！");
        }

        if(!generatorPersistence.getIsOpen() && !generatorPersistence.getDeveloper().getId().equals(userId)){
            throw new AppException("当前生成器正在维护，请暂停操作，等待发布！");
        }

        dataModelRepository.delete(dataModelPersistence, rootPersistence);

        generatorInstancePersistence.setModifyDate(new Date());
        generatorInstanceRepository.update(generatorInstancePersistence);

        return dataModelPersistence;
    }

    @Override
    public DataModel get(DataModelGetRequest request) {
        Long id = request.getId();

        Long rootId = request.getRootId();
        DataModel rootPersistence = dataModelRepository.selectById(rootId);
        if(rootPersistence == null){
            throw new AppException("root不存在");
        }

        DataModel dataModelPersistence = dataModelRepository.selectById(id, rootPersistence);
        if(dataModelPersistence == null) {
            throw new AppException("数据不存在");
        }

        DataModel dataModel = new DataModel();
        dataModel.setId(dataModelPersistence.getId());
        dataModel.setName(dataModelPersistence.getName());
        dataModel.setCreateDate(dataModelPersistence.getCreateDate());
        dataModel.setModifyDate(dataModelPersistence.getModifyDate());
        dataModel.setProperties(dataModelPersistence.getProperties());
        dataModel.setAssociation(dataModelPersistence.getAssociation());
        dataModel.setIsExpand(dataModelPersistence.getIsExpand());
        dataModel.setDynamicModel(dataModelPersistence.getDynamicModel());
        dataModel.setGeneratorInstance(dataModelPersistence.getGeneratorInstance());
        dataModel.setParent(dataModelPersistence.getParent());
        User userPersistence = userRepository.selectById(dataModelPersistence.getUser().getId());
        dataModel.setUser(userPersistence);

        return dataModel;
    }

    @Override
    public DataModel update(DataModelUpdateRequest request) {
        Long id = request.getId();
        Long userId = request.getAuthentication().getUserId();

        Long rootId = request.getRootId();
        DataModel rootPersistence = dataModelRepository.selectById(rootId);
        if(rootPersistence == null){
            throw new AppException("root不存在");
        }

        if(!userId.equals(rootPersistence.getUser().getId())){
            throw new AppException("权限不足");
        }

        GeneratorInstance generatorInstancePersistence = generatorInstanceRepository.selectById(rootPersistence.getGeneratorInstance().getId());
        if(generatorInstancePersistence == null){
            throw new AppException("实例不存在");
        }

        DataModel dataModelPersistence = dataModelRepository.selectById(id, rootPersistence);
        if(dataModelPersistence == null) {
            throw new AppException("数据不存在");
        }

        Generator generatorPersistence = generatorRepository.selectById(dataModelPersistence.getGenerator().getId());
        if(generatorInstancePersistence.getVersion() < generatorPersistence.getVersion()){
            throw new AppException("当前生成器已升级发布，请刷新数据，重新操作！");
        }

        if(!generatorPersistence.getIsOpen() && !generatorPersistence.getDeveloper().getId().equals(userId)){
            throw new AppException("当前生成器正在维护，请暂停操作，等待发布！");
        }

        dataModelPersistence.setIsExpand(request.getIsExpand());
        dataModelPersistence.setName(request.getName());
        dataModelPersistence.setModifyDate(new Date());
        dataModelPersistence.setProperties(request.getProperties());
        dataModelPersistence.setAssociation(request.getAssociation());
        dataModelRepository.update(dataModelPersistence);

        generatorInstancePersistence.setModifyDate(new Date());
        generatorInstanceRepository.update(generatorInstancePersistence);

        return dataModelPersistence;
    }

    @Override
    public DataModel tree(DataModelTreeRequest request) {
        Long rootId = request.getRootId();
        DataModel rootPersistence = dataModelRepository.selectById(rootId);
        if(rootPersistence == null){
            throw new AppException("root不存在");
        }

        DataModel root = new DataModel();
        root.setId(rootPersistence.getId());
        root.setName(rootPersistence.getName());
        root.setIsExpand(rootPersistence.getIsExpand());
        root.setDynamicModel(rootPersistence.getDynamicModel());

        LinkedList<DataModel> sourceStack = new LinkedList<>();
        sourceStack.push(rootPersistence);
        LinkedList<DataModel> targetStack = new LinkedList<>();
        targetStack.push(root);
        while (!sourceStack.isEmpty()) {
            DataModel sourceDataModel = sourceStack.pop();
            DataModel targetDataModel = targetStack.pop();
            sourceDataModel.getChildren().forEach(dataModel -> {
                sourceStack.push(dataModel);
                DataModel newDataModel = new DataModel();
                newDataModel.setId(dataModel.getId());
                newDataModel.setName(dataModel.getName());
                newDataModel.setIsExpand(dataModel.getIsExpand());
                newDataModel.setSequence(dataModel.getSequence());
                newDataModel.setDynamicModel(dataModel.getDynamicModel());
                newDataModel.setUser(dataModel.getUser());
                targetDataModel.getChildren().add(newDataModel);
                targetStack.push(newDataModel);
            });
        }

        targetStack.push(root);
        while (!targetStack.isEmpty()) {
            DataModel targetDataModel = targetStack.pop();
            List<DataModel> children = targetDataModel.getChildren();
            if(!children.isEmpty()){
                children.sort((d1, d2) -> d1.getSequence() - d2.getSequence());
            }
            targetDataModel.getChildren().forEach(targetStack::push);
        }

        return root;
    }

    @Override
    public DataModel sort(DataModelSortRequest request) {
        Long rootId = request.getRootId();
        DataModel rootPersistence = dataModelRepository.selectById(rootId);
        if(rootPersistence == null){
            throw new AppException("root不存在");
        }

        Long id = request.getId();
        DataModel dataModelPersistence = dataModelRepository.selectById(id, rootPersistence);
        if(dataModelPersistence == null) {
            throw new AppException("数据不存在");
        }

        Long userId = request.getAuthentication().getUserId();
        if(!userId.equals(dataModelPersistence.getUser().getId())){
            throw new AppException("权限不足");
        }

        Long parentId = dataModelPersistence.getParent().getId();
        DataModel parentPersistence = dataModelRepository.selectById(parentId, rootPersistence);
        if(parentPersistence == null){
            throw new AppException("parent不存在");
        }

        GeneratorInstance generatorInstancePersistence = generatorInstanceRepository.selectById(rootPersistence.getGeneratorInstance().getId());
        if(generatorInstancePersistence == null){
            throw new AppException("实例不存在");
        }

        List<DataModel> children = parentPersistence.getChildren();
        int fromIndex = request.getFromIndex();
        int toIndex = request.getToIndex();
        int size = children.size();
        if(size == 0 || toIndex > size - 1 || fromIndex > size - 1){
            throw new AppException("请保存并刷新数据，重新操作！");
        }
        children.sort((d1, d2) -> d1.getSequence() - d2.getSequence());
        if(dataModelPersistence != children.remove(fromIndex)){
            throw new AppException("请保存并刷新数据，重新操作！");
        }
        children.add(toIndex, dataModelPersistence);

        int sequence = 1;
        for(DataModel d : children){
            if(sequence != d.getSequence()){
                d.setSequence(sequence);
                d.setModifyDate(new Date());
                dataModelRepository.update(d);
            }
            sequence++;
        }

        generatorInstancePersistence.setModifyDate(new Date());
        generatorInstanceRepository.update(generatorInstancePersistence);

        return dataModelPersistence;
    }

}