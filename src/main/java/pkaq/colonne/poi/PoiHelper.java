package pkaq.colonne.poi;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 处理山西省煤炭厅
 */
public class PoiHelper {
    private final static String HORIZONTAL = "horizontal";
    private final static String VERTICAL = "vertical";

    private final String path = "d:/ID.xls";
    private final String reSavePath = "d:/id-2.xls";

    public static void main(String[] args) throws IOException, InvalidFormatException {
            new PoiHelper().importData();
//        new PoiHelper().getLouTian();
    }
    // 导入数据
    public void importData() {
        try(
                //入口文件
                InputStream inp = new FileInputStream(path)
        ){
            Workbook workbook = WorkbookFactory.create(inp);
            //获得sheet页个数
            int sheetNum = workbook.getNumberOfSheets();
            System.out.println("sheet页数 :" + sheetNum);
            //遍历sheet页 整理数据
            for (int i = 1; i < (sheetNum - 25); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                //矿井基本信息
                List<String> bsaeList = this.packBaseData(sheet);
                // 获取煤层基本信息
                List<List<String>> mcList =  this.getSheetData(sheet,10,21,3,10,PoiHelper.VERTICAL,"煤层基本信息");
                // ------------------------------------------------------------------------------------------------------------------------------- //
                // 获取本矿所有证件信息
                List<List<String>> zjList = this.getSheetData(sheet,16,16,1,5,PoiHelper.HORIZONTAL,"证件信息");
                // ------------------------------------------------------------------------------------------------------------------------------- //
                // 获取本矿所有井筒信息
                List<List<String>> jtList = this.getSheetData(sheet,4,36,2,7,PoiHelper.HORIZONTAL,"井筒信息");
                List<List<String>> jtList_2 = this.getSheetData(sheet,4,36,6,7,PoiHelper.HORIZONTAL,"井筒信息");
                // ------------------------------------------------------------------------------------------------------------------------------- //

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }
    // 读取单元格内容并以字符串方式返回
    public String getRCValueAsString(Sheet sheet , int rowIndex , int columnIndex){
        Cell cell = sheet.getRow(rowIndex).getCell(columnIndex);

        String cellvalue = "";
        if (null != cell) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    cellvalue = "";
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    cellvalue = Boolean.toString(cell.getBooleanCellValue());
                    break;
                // 数值
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getDateCellValue());
                    } else {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        String temp = cell.getStringCellValue();
                        // 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串
                        if (temp.indexOf(".") > -1) {
                            cellvalue = String.valueOf(new Double(temp)).trim();
                        } else {
                            cellvalue = temp.trim();
                        }
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    cellvalue = cell.getStringCellValue().trim();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    cellvalue = "";
                    break;
                default:
                    cellvalue = cell.getRichStringCellValue().getString();
            }
        }
        return cellvalue;
    }
    // 获取露天矿井
    public void getLouTian(){
        try(//入口文件
            InputStream inp = new FileInputStream(path)
        ) {

            Workbook workbook = WorkbookFactory.create(inp);

            //获得sheet页个数
            int sheetNum = workbook.getNumberOfSheets();
            System.out.println("sheet页数 :" + sheetNum);

            List<String> nameList = new ArrayList<>();

            // 读取第四行第一列单元格内容作为sheet页的名字
            // 并统计重复条数
            for (int i = 1; i < (sheetNum - 25); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Row row = sheet.getRow(3);
                Cell cell = row.getCell(0);

                String name = cell.getRichStringCellValue().getString();
                if (name.contains("露天")){
                    System.out.println("露天矿:"+sheet.getSheetName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**按行或列读取数据 组装成list返回
     * @param sheet         sheet对象
     * @param arrayLength   列数
     * @param row           起始行
     * @param column        起始列
     * @param step          读取步长
     * @param direction     延展方向
     * @return
     */
    public List<List<String>> getSheetData(Sheet sheet, int arrayLength , int row ,int column , int step , String direction , String desc){
        List<List<String>> list = new ArrayList();

        int loopMember = row;
        int loopInner = column;
        int loopTime = row + step;


        if (PoiHelper.VERTICAL.equals(direction)) {
            loopMember = column;
            loopInner = row;
            loopTime = column + step;
        }

        for( ; loopMember < loopTime ; loopMember++ ){
            List<String> val = new ArrayList<>();
            for (int i = 0; i < arrayLength; i++) {

                // 水平延展
                if (direction.equals(PoiHelper.HORIZONTAL)) {
                    val.add(this.getRCValueAsString(sheet,loopMember,loopInner+i));
                }
                // 纵向延展
                if (direction.equals(PoiHelper.VERTICAL)) {
                    val.add(this.getRCValueAsString(sheet,loopInner+i,loopMember));
                }
            }
            // 剔除空的数据
            Predicate<String> pred = str -> "".equals(str);
            val.removeIf(pred);

            list.add(val);
        }

        return list;
    }
    // 整理矿井基本信息(井工矿)
    public List<String> packBaseData(Sheet sheet){
        List<String> baseList = new ArrayList<>();
        //档案编号
        String title = this.getRCValueAsString(sheet,3,0);
        String mkgldabh = title.substring(title.indexOf("管理档案（")).replace(")","").trim();
        //公告文号
        String ggwh =  this.getRCValueAsString(sheet,53,0);
        ggwh = ggwh.substring(ggwh.indexOf("〕第"),ggwh.indexOf("号公告"));
        //隶属企业
        String lsqy = this.getRCValueAsString(sheet,6,2);
        // 煤矿名称
        String mkmc = title.substring(title.indexOf(lsqy),title.indexOf("（井工）"));
        // 详细地址
        String xxdz = this.getRCValueAsString(sheet,5,2);
        // 煤矿性质
        List<List<String>> mkxzList = this.getSheetData(sheet,15,7,2,1,PoiHelper.HORIZONTAL,"煤矿性质");
        int checkedIndex = mkxzList.get(0).indexOf("是");
        String mkxz = mkxzList.get(0).get(checkedIndex-1);

        String sfzhkj = this.getRCValueAsString(sheet,8,3);
        String zhqk = "";
        String zhsl = "";
        if (StringUtils.isNotBlank(sfzhkj)){
            sfzhkj = "是";
            zhqk = "单独保留";
        } else {
            String desc = this.getRCValueAsString(sheet,8,5);
            sfzhkj = "否";
            zhqk = "重组整合";
            zhsl = desc.substring(desc.indexOf("由"),desc.indexOf("个"));
        }
        //生产能力（万吨/年）
        String scnl = this.getRCValueAsString(sheet,10,1);
        // 开拓方式
        String ktfs = this.getRCValueAsString(sheet,10,2);
        // 生产采煤工作面个数
        String cmgzmgs = this.getRCValueAsString(sheet,9,7);
        //采煤工艺
        List<List<String>> cmgyList = this.getSheetData(sheet,2,10,8,5,PoiHelper.HORIZONTAL,"采煤工艺");
        String cmgy = "";
        for( int x = 0 , size = cmgyList.size(); x<size ; x++){
            List list = cmgyList.get(x);
            if (list.size()>1){
                cmgy = cmgyList.get(x).get(0);
                break;
            }
        }
        //水平个数
        String spgs = this.getRCValueAsString(sheet,12,1);
        //水平标高
        String spbg = this.getRCValueAsString(sheet,12,2);
        // 从业人员数量
        String cyrysl = this.getRCValueAsString(sheet,12,3);
        //原煤生产人员数量
        String ymscrysl = this.getRCValueAsString(sheet,14,1);
        //特种作业人员数量
        String tzzyrysl = this.getRCValueAsString(sheet,14,3);
        //采区及设计采区回采率
        String cqsjcqhcl = this.getRCValueAsString(sheet,12,5);
        //矿井瓦斯等级
        List<List<String>> kjwsdjList = this.getSheetData(sheet,15,31,2,1,PoiHelper.HORIZONTAL,"瓦斯等级");
        int wsCheckedIndex = kjwsdjList.get(0).indexOf("是");
        String kjwsdj = kjwsdjList.get(0).get(checkedIndex-1);
        //水文地质类型
        List<List<String>> swdzList = this.getSheetData(sheet,15,31,2,1,PoiHelper.HORIZONTAL,"水文地质类型");
        int swCheckedIndex = swdzList.get(0).indexOf("是");
        String swdzlx = swdzList.get(0).get(checkedIndex-1);
        // 井筒数量
        String jtsl = this.getRCValueAsString(sheet,35,3);
        // 通风井数量
        String tfjsl = this.getRCValueAsString(sheet,35,5);
        // 回风井数量
        String hfjsl = this.getRCValueAsString(sheet,35,7);
        // 辅助提升井筒数量
        String fztsjtsl = this.getRCValueAsString(sheet,35,7);
        //主提升装备型号
        String ztszbxh = this.getRCValueAsString(sheet,44,4);
        //辅助提升装备型号
        String fztszbxh = this.getRCValueAsString(sheet,44,8);
        //主运输装备型号
        String zyszbxh = this.getRCValueAsString(sheet,45,4);
        //辅助运输装备型号
        String fzyszbxh = this.getRCValueAsString(sheet,45,8);
        //通风方式
        String tffs = this.getRCValueAsString(sheet,46,4);
        //矿井总回风量m3/min
        String kjzhfl = this.getRCValueAsString(sheet,46,8);
        //地面瓦斯抽采能力m3/min
        String dmwsccnl = this.getRCValueAsString(sheet,47,4);
        //井下瓦斯抽采能力m3/min
        String jxwsccnl = this.getRCValueAsString(sheet,47,8);
        //煤矿供电总负荷KVA
        String gjzfh = this.getRCValueAsString(sheet,48,4);
        //回采设备最高电压等级
        String zgdydj = this.getRCValueAsString(sheet,48,8);
        //主排水设备型号
        String zpsshxh = this.getRCValueAsString(sheet,49,4);
        //主排水设备管路型号X趟数
        String zpsglxhts = this.getRCValueAsString(sheet,49,6);
        //最大排水能力m3/h
        String zdpsnl = this.getRCValueAsString(sheet,49,8);
        //强排设备型号
        String qpsbxh = this.getRCValueAsString(sheet,50,4);
        //强排设备管路型号X趟数
        String qpsbglxhts = this.getRCValueAsString(sheet,50,6);
        //强排系统能力m3/h
        String qpxtnl = this.getRCValueAsString(sheet,50,8);
        //煤矿类型
        String mklx = "井工";
        //打印时间
        String dysj = this.getRCValueAsString(sheet,59,0);

        return baseList;
    }
    // 重命名sheet页
    public void renameSheet() {
        try(
            //入口文件
            InputStream inp = new FileInputStream(path);
            // 另存修改后的sheet
            FileOutputStream stream = new FileOutputStream(new File(reSavePath))) {

            Workbook workbook = WorkbookFactory.create(inp);

            //获得sheet页个数
            int sheetNum = workbook.getNumberOfSheets();
            System.out.println("sheet页数 :" + sheetNum);

            List<String> nameList = new ArrayList<>();

            List<String> repeatNameList = new ArrayList<>();
            // 读取第四行第一列单元格内容作为sheet页的名字
            // 并统计重复条数
            for (int i = 1; i < (sheetNum - 25); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Row row = sheet.getRow(3);
                Cell cell = row.getCell(0);

                String name = cell.getRichStringCellValue().getString();
                String finalname = name.substring(0, name.indexOf("生产能力"));

                if (nameList.contains(finalname)) {
                    repeatNameList.add(finalname);
                }
                nameList.add(finalname);

                workbook.setSheetName(i, finalname);
            }

            System.out.println("重复条数 : " + repeatNameList.size());


            workbook.write(stream);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
