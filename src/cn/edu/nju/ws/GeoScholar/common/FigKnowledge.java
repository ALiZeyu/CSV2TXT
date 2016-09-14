package cn.edu.nju.ws.GeoScholar.common;

import cn.edu.nju.ws.GeoScholar.basis.PredFact;
import cn.edu.nju.ws.GeoScholar.basis.data.ResourceModel;
import cn.edu.nju.ws.GeoScholar.basis.data.ResourceRAMModel;
import cn.edu.nju.ws.GeoScholar.basis.rdf.RDFFact;
import cn.edu.nju.ws.GeoScholar.basis.rdf.impl.RDFTempModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 图标注知识结构，表示每个图的标注知识
 *
 * @author XinqiQian
 */
public class FigKnowledge {
    /**
     *图名
     */
    String figureID;
    /**
     * 图标注文件对应文本字符串列表
     */
    ArrayList<String> text_list;
    /**
     * 图标注文件对应rdf数据模型(过期)
     */
    @Deprecated
    RDFTempModel fig_model;
    /**
     * 图标注文件对应谓词表示数据模型
     */
    ResourceModel model;



    public FigKnowledge() {
        this.text_list = new ArrayList<>();
        fig_model = new RDFTempModel();
        this.model = new ResourceRAMModel();
    }

    public FigKnowledge(String figureID) {
        this.figureID = figureID;
        this.text_list = new ArrayList<>();
        fig_model = new RDFTempModel();
        this.model = new ResourceRAMModel();
    }

    @Deprecated
    public FigKnowledge(ArrayList<String> text_list, RDFTempModel fig_model) {
        this.text_list = text_list;
        this.fig_model = fig_model;
        this.model = new ResourceRAMModel();
    }

    public FigKnowledge(ArrayList<String> text_list, ResourceModel figModel) {
        this.text_list = text_list;
        this.model = figModel;
        this.fig_model = new RDFTempModel();
    }

    /**
     * 图标注知识存储类
     * @param figureID 图名
     * @param text_list 图标注文件对应文本字符串列表
     * @param figModel 图标注文件对应谓词表示数据模型
     * @
     */
    public FigKnowledge(String figureID, ArrayList<String> text_list, ResourceModel figModel) {
        this.figureID = figureID;
        this.text_list = text_list;
        this.model = figModel;
        this.fig_model = new RDFTempModel();
    }

    @Deprecated
    public FigKnowledge(String figureID, ArrayList<String> text_list, RDFTempModel fig_model) {
        this.figureID = figureID;
        this.text_list = text_list;
        this.fig_model = fig_model;
        this.model = new ResourceRAMModel();
    }

    /**
     * 获取图标注数据对应的图名
     * @return 图名
     */
    public String getFigureID() {
        return figureID;
    }

    /**
     * 获取图标注数据对应的数据模型(过期)
     * @return RDFTempModel类型 rdf model
     */
    @Deprecated
    public RDFTempModel getFig_model() {
        return fig_model;
    }

    /**
     * 获取图标注数据对应的数据模型
     * @return ResourceModel类型  谓词表示数据模型model
     */
    public ResourceModel getFigModel() {
        return model;
    }

    /**
     * 获取图标注文件对应文本字符串列表
     * @return ArrayList<String> text_list 文本字符串列表
     */
    public ArrayList<String> getText_list() {
        return text_list;
    }

    /**
     * 返回图的信息
     * @return Fig Knowledge structure for Figure: $figure's_Name
     */
    public String toString() {
        return "Fig Knowledge structure for Figure:" + figureID;
    }
}
