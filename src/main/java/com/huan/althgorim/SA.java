package com.huan.althgorim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import com.huan.course.util.ConvertUtil;
import com.huan.definition.ConstantVal;
import com.huan.definition.MyRandom;
import com.huan.definition.Mytime;
import com.huan.definition.Position;
import com.huan.definition.ResultType;
import com.huan.exception.Myexception;
import com.huan.model.HalfTeacher;
import com.huan.model.WholeTeacher;
import com.huan.sort.util.ProcessUtil;
import com.huan.model.Teacher;

public class SA {
	private static final Logger logger = Logger.getLogger(SA.class);

	private int N;// 每个温度迭代步长
	private int T;// 降温次数
	private double a;// 降温系数
	private double t0;// 初始温度

	private Random random;
	public int needLessons;
	public int lessonNum;
	public boolean noConflict = false;
	public boolean success = false;// have answer
	public int[][] sheetInfor;
	public List<Teacher> datas;
	public List<WholeTeacher> wholeTeachers;
	public List<HalfTeacher> halfTeachers;
	public ArrayList<Integer[]> definedCost;
	public ResultType bestResult;
	public double minCost = 99999999;
	public double tempCost;
	public double mycost;
	public ResultType tempResult;
	public ResultType beginResult;
	public ArrayList<ArrayList<Integer>> classIncludeTeacher;
	public int classNum;
	public boolean everyWeek[];
	public int fixTable[][];
	public ArrayList<Double> bestCostflu = new ArrayList<>();
	public ArrayList<Double> tempCostflu = new ArrayList<>();

	// 程序索引转数据库索引
	private Map<Integer, Integer> pgi2dbi = new HashMap<>();
	// 数据库索引转程序索引
	private Map<Integer, Integer> dbi2pgi = new HashMap<>();
	// 程序索引转单双周程序索引
	private Map<Integer, Integer> pgi2half = new HashMap<>();
	// 程序索引转全周索引
	private Map<Integer, Integer> pgi2whole = new HashMap<>();

	private int[][] oddSheet;
	private int[][] evenSheet;
	/** true:even false:odd */
	private boolean alter[];

	/**
	 * constructor of GA
	 * 
	 * @param t
	 *            降温次数
	 * @param n
	 *            每个温度迭代步长
	 * @param tt
	 *            初始温度
	 * @param aa
	 *            降温系数
	 * @param datas
	 *            老师信息
	 * @param definedCost
	 *            课程安排在不同课时产生的代价
	 * @param classIncludeTeacher
	 *            各班级包含的老师索引号
	 * 
	 **/
	public SA(int n, int t, double tt, double aa, List<Teacher> datas, ArrayList<Integer[]> definedCost,
			ArrayList<ArrayList<Integer>> classIncludeTeacher, int needLessons, int lessonNum, int classNum,
			boolean[] everyWeek) {
		N = n;
		T = t;
		t0 = tt;
		a = aa;
		random = MyRandom.getInstance();
		sheetInfor = new int[classNum][needLessons];
		// this.datas=new ArrayList<>(datas);
		this.datas = datas;
		halfTeachers = new ArrayList<>();
		wholeTeachers = new ArrayList<>();
		int count = 0;
		int half = 0;
		int whole = 0;
		for (Teacher each : datas) {
			pgi2dbi.put(count, each.teacherIndex);
			dbi2pgi.put(each.teacherIndex, count);

			if (each instanceof HalfTeacher) {
				pgi2half.put(count, half++);
				halfTeachers.add((HalfTeacher) each);
			} else {
				pgi2whole.put(count, whole++);
				wholeTeachers.add((WholeTeacher) each);
			}
			count++;
		}
		this.definedCost = definedCost;
		this.classIncludeTeacher = classIncludeTeacher;

		this.needLessons = needLessons;
		this.lessonNum = lessonNum;
		this.classNum = classNum;
		this.everyWeek = everyWeek;

		bestResult = new ResultType(classNum, lessonNum, everyWeek);

		this.tempResult = new ResultType(classNum, lessonNum, everyWeek);
		this.beginResult = new ResultType(classNum, lessonNum, everyWeek);
		this.fixTable = new int[this.classNum][this.lessonNum * 7];
		this.oddSheet = new int[this.classNum][this.lessonNum * 7];
		this.evenSheet = new int[this.classNum][this.lessonNum * 7];
		this.alter = new boolean[classNum];

		for (int i = 0; i < classNum; i++) {
			alter[i] = random.nextBoolean();
		}
	}

	// public class HalfCourse implements Comparable<HalfCourse>{
	//
	//
	// private String courseName;
	// private int perWeekLessons;
	//
	//
	// public String getCourseName() {
	// return courseName;
	// }
	//
	//
	// public void setCourseName(String courseName) {
	// this.courseName = courseName;
	// }
	//
	//
	// public int getPerWeekLessons() {
	// return perWeekLessons;
	// }
	//
	//
	// public void setPerWeekLessons(int perWeekLessons) {
	// this.perWeekLessons = perWeekLessons;
	// }
	//
	//
	// @Override
	// public int compareTo(HalfCourse o) {
	// return perWeekLessons-o.getPerWeekLessons();
	// }
	//
	// @Override
	// public boolean equals(Object o){
	// String itsName=(String)o;
	// return courseName.equals(itsName);
	// }
	//
	//
	// public HalfCourse(String courseName, int perWeekLessons) {
	// this.courseName = courseName;
	// this.perWeekLessons = perWeekLessons;
	// }
	//
	//
	// }
	//
	// public List<HalfCourse> halfDifCourse() {
	// if(halfTeachers.size()==0){
	// return null;
	// }
	// boolean sameFlag = false;
	// List<HalfCourse>recordCourses=new ArrayList<>();
	// recordCourses.add(new
	// HalfCourse(halfTeachers.get(0).courseName,halfTeachers.get(0).perWeekClassNum));
	// int teacherNum=halfTeachers.size();
	// for (int i = 0; i < teacherNum; i++) {
	// sameFlag = false;
	// for (int j = 0; j < recordCourses.size(); j++) {
	// if
	// (halfTeachers.get(i).courseName.equals(recordCourses.get(j).getCourseName()))
	// {
	// sameFlag = true;
	// break;
	//
	// }
	// }
	// if (!sameFlag) {
	// recordCourses.add(new
	// HalfCourse(halfTeachers.get(i).courseName,halfTeachers.get(i).perWeekClassNum));
	// }
	// }
	// return recordCourses;
	// }

	public void arrangeHalf() {
		// List<HalfCourse>recordCourses=halfDifCourse();
		if (halfTeachers.size() != 0) {
			// Collections.sort(recordCourses);
			setBlankEmpty(oddSheet);
			setBlankEmpty(evenSheet);
			for (HalfTeacher teacher : halfTeachers) {
				arrangeOneHalfTeacher(teacher, oddSheet, evenSheet);
			}
			makeHalfWork();
		}

	}

	
	public void makeHalfWork(){
//		while(!isHalfConflict()){
//			
//		}
		
	}
	
	public void setBlankEmpty(int sheet[][]) {
		int row = sheet.length;
		int col = sheet[0].length;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				sheet[i][j] = ConstantVal.BLANK_EMPTY;
			}
		}
	}

	public void arrangeOneHalfTeacher(HalfTeacher teacher, int oddSheet[][], int evenSheet[][]) {
		ArrayList<Integer> myClasses = (ArrayList<Integer>) teacher.classes;
		for (int oneClass : myClasses) {
			if (alter[oneClass] == false) {//odd
				
			} else {//even

			}
			alter[oneClass] = !alter[oneClass];
		}

	}

	public void initWhole() {

		boolean visited[] = new boolean[everyWeek.length];

		for (int i = 0; i < classIncludeTeacher.size(); i++) {// 换班级
			ArrayList<Integer> teacherIndex = classIncludeTeacher.get(i);

			for (int k = 0; k < everyWeek.length; k++) {
				visited[k] = everyWeek[k];
			}
			for (int t = 0; t < teacherIndex.size(); t++) {// 换班级中的老师
				Teacher et = datas.get(teacherIndex.get(t));
				if (et instanceof HalfTeacher)
					continue;
				WholeTeacher myTeacher = (WholeTeacher) et;
				ProcessUtil wholePro = myTeacher.wholePro;
				wholePro.classes = myTeacher.classes;
				ArrayList<Integer> YConflict = new ArrayList<>();
				int lessons = myTeacher.perWeekTimeNum;
				Integer certainCost[] = definedCost.get(myTeacher.courseIndex);

				for (int loop = 0; loop < lessons; loop++) {// 选课时

					HashMap<Integer, Double> suitInfor = new HashMap<>();
					double sumSuit = 0;
					for (int k = 0; k < visited.length; k++) {
						if (visited[k] == false) {
							double aloneSuit = 60.0 / (certainCost[k % lessonNum] + additionCost(visited, k)
									+ conflictCost(wholePro, k));
							suitInfor.put(k, aloneSuit);
						}
					}

					for (Entry<Integer, Double> entry : suitInfor.entrySet()) {
						sumSuit += entry.getValue();
					}

					double selectR = random.nextDouble() * sumSuit;
					double indual = 0;
					int n = -1;
					for (Entry<Integer, Double> entry : suitInfor.entrySet()) {
						indual += entry.getValue();
						if (indual > selectR) {
							n = entry.getKey();
							break;
						}
					}

					visited[n] = true;
					if (wholePro.weekY.contains(n) && !YConflict.contains(n)) {
						YConflict.add(n);
						// myTeacher.conflictPosition.add(new Position(i, n));
					} else {
						wholePro.weekY.add(n);
					}
					wholePro.arrangeCells.add(new Position(i, n));

				}

				for (int h = 0; h < YConflict.size(); h++) {
					for (int s = 0; s < wholePro.arrangeCells.size(); s++) {
						if (wholePro.arrangeCells.get(s).timeY == YConflict.get(h)) {
							if (!wholePro.conflictCells.contains(wholePro.arrangeCells.get(s)))
								wholePro.conflictCells.add(wholePro.arrangeCells.get(s));
						}
					}
				}

			}

		}

		for (int i = 0; i < datas.size(); i++) {
			Teacher theTea = datas.get(i);
			if (theTea instanceof HalfTeacher) {
				continue;
			}
			ProcessUtil whoPro = ((WholeTeacher) theTea).wholePro;
			for (int j = 0; j < whoPro.arrangeCells.size(); j++) {
				Position temp = whoPro.arrangeCells.get(j);
				sheetInfor[temp.classX][temp.timeY] = i;
			}
		}

		// printSheet(sheetInfor);

		// for (int i = 0; i < 15; i++) {
		// for (int j = 0; j < needLessons - 2; j++) {
		// if (sheetInfor[i][j] == sheetInfor[i][j + 1] && sheetInfor[i][j] ==
		// sheetInfor[i][j + 2]
		// && j % lessonNum != 6 && j % lessonNum != 5) {
		// datas.get(sheetInfor[i][j]).connectLessons.add(new Position(i, j));
		// datas.get(sheetInfor[i][j]).connectLessons.add(new Position(i, j +
		// 1));
		// datas.get(sheetInfor[i][j]).connectLessons.add(new Position(i, j +
		// 2));
		//
		// } else if (sheetInfor[i][j] == sheetInfor[i][j + 1] && j % lessonNum
		// != 6) {
		// datas.get(sheetInfor[i][j]).connectLessons.add(new Position(i, j));
		// datas.get(sheetInfor[i][j]).connectLessons.add(new Position(i, j +
		// 1));
		// }
		//
		// }
		// if (sheetInfor[i][needLessons - 1] == sheetInfor[i][needLessons - 2]
		// && sheetInfor[i][needLessons - 1] == sheetInfor[i][needLessons - 3])
		// {
		//
		// } else if (sheetInfor[i][needLessons - 1] ==
		// sheetInfor[i][needLessons - 2]) {
		// datas.get(sheetInfor[i][needLessons - 1]).connectLessons.add(new
		// Position(i, needLessons - 1));
		// datas.get(sheetInfor[i][needLessons - 1]).connectLessons.add(new
		// Position(i, needLessons - 2));
		// }
		// }

		// searchConnection(new ResultType(datas, sheetInfor));
		// printConflict(datas);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Teacher> void searchConnection(int sheetInfor[][], List<T> oneTypeTeacher, int type)
			throws Myexception {

		List targetTeacher = null;
		List<ProcessUtil> myDatas = new ArrayList<>();
		Map<Integer, Integer> convert = null;
		switch (type) {
		case ConstantVal.PROCESS_WHOLE:
			targetTeacher = (List<WholeTeacher>) oneTypeTeacher;
			for (WholeTeacher wt : (List<WholeTeacher>) targetTeacher) {
				myDatas.add(wt.wholePro);
			}
			convert = pgi2whole;
			break;
		case ConstantVal.PROCESS_EVEN:
			targetTeacher = (List<HalfTeacher>) oneTypeTeacher;
			for (HalfTeacher wt : (List<HalfTeacher>) targetTeacher) {
				myDatas.add(wt.evenUtil);
			}
			convert = pgi2half;
			break;
		case ConstantVal.PROCESS_ODD:
			targetTeacher = (List<HalfTeacher>) oneTypeTeacher;
			for (HalfTeacher wt : (List<HalfTeacher>) targetTeacher) {
				myDatas.add(wt.oddUtil);
			}
			convert = pgi2half;
			break;
		default:
			throw new Myexception(String.format("searchConnection搜索连堂时异常，不支持处理%s类型", type));
		}
		for (int i = 0; i < myDatas.size(); i++) {
			myDatas.get(i).connectCells.clear();
		}

		for (int i = 0; i < classNum; i++) {
			for (int j = 0; j < lessonNum * 7;) {
				if (everyWeek[j] == false) {
					int count = 1;
					int current = sheetInfor[i][j];
					if (current == -1)
						continue;
					Position temp = new Position(i, j);
					ProcessUtil focus = myDatas.get(convert.get(current));
					focus.connectCells.add(temp);
					while (count + j < lessonNum * 7) {
						if (current == sheetInfor[i][count + j]) {
							focus.connectCells.add(new Position(i, count + j));
							count++;
						} else {
							if (count == 1) {
								focus.connectCells.remove(temp);
							}
							break;
						}
					}
					if (count + j == lessonNum * 7 && count == 1) {
						focus.connectCells.remove(temp);
					}
					j += count;
				} else {
					j++;
				}

			}
		}
	}

	public void printSheet(int sheet[][]) {
		logger.info("\r\n\t");
		for (int i = 0; i < classNum; i++) {
			logger.info(i + "\t");
		}
		logger.info("\r\n");
		for (int j = 0; j < needLessons; j++) {
			logger.info((j) + "\t");
			if (everyWeek[j] == false) {

				for (int i = 0; i < classNum; i++) {
					String temp = String.format("%s\t", datas.get(sheet[i][j]).teacherName);
					logger.info(temp);
				}

			}
			logger.info("\r\n");
		}

	}

	public double additionCost(boolean visited[], int k) {

		double res = 0;
		int dayIndex = (k / lessonNum) * lessonNum;
		int minL = lessonNum;
		try {
			for (int i = dayIndex; i < dayIndex + lessonNum; i++) {

				if (visited[i] == true && i != k) {
					minL = Math.min(minL, Math.abs(i - k));
				}
			}
		} catch (Exception e) {

		}
		res = (double) minL;
		if (minL == 1) {
			return 500.0;
		}
		return lessonNum - res / 1.5;

	}

	public double conflictCost(ProcessUtil myTeacher, int k) {
		double res = 0;

		if (myTeacher.weekY.contains(k)) {
			res = 500.0;
		}

		return res;

	}

	// public void printConflict(ArrayList<WholeTeacher> datas) {
	// for (int i = 0; i < datas.size(); i++) {
	// System.out.print("name:" + datas.get(i).teacherName);
	// System.out.print(" course:" + datas.get(i).courseName);
	// System.out.print(" totalLessonsL:" + datas.get(i).perWeekClassNum *
	// datas.get(i).perWeekTimeNum);
	// System.out.println();
	// System.out.println("normal");
	// for (int k = 0; k < datas.get(i).arrangeCells.size(); k++) {
	// Position tPosition = datas.get(i).arrangeCells.get(k);
	// System.out.print("(" + tPosition.classX + "," + tPosition.timeY + ")");
	// }
	// System.out.println();
	// System.out.print("conflict: ");
	// for (int k = 0; k < datas.get(i).conflictCells.size(); k++) {
	// Position tPosition = datas.get(i).conflictCells.get(k);
	// System.out.print("(" + tPosition.classX + "," + tPosition.timeY + ")");
	// }
	// System.out.println();
	// System.out.print("connect: ");
	// for (int k = 0; k < datas.get(i).connectCells.size(); k++) {
	// Position tPosition = datas.get(i).connectCells.get(k);
	// System.out.print("(" + tPosition.classX + "," + tPosition.timeY + ")");
	// }
	// System.out.println();
	//
	// int ttttt = datas.get(i).arrangeCells.size() +
	// datas.get(i).conflictCells.size();
	// if (ttttt == datas.get(i).perWeekClassNum * datas.get(i).perWeekTimeNum)
	// System.out.println("this teacher arrange" + ttttt + "lessons");
	// else {
	// System.err.println("this teacher arrange" + ttttt + "lessons");
	// }
	// System.out.println();
	// }
	//
	// }

	public void changeAttr(int table[][], String exchange[], int sheet[][]) throws Myexception {
		this.fixTable = table;
		this.sheetInfor = sheet;
		for (int i = 0; i < exchange.length; i++) {
			if (exchange[i].length() != 0) {
				String opers[] = exchange[i].split("&");
				for (String oper : opers) {
					int twoNum[] = ConvertUtil.parsIntArray(oper.split("="));
					int temp = sheetInfor[i][twoNum[0]];
					sheetInfor[i][twoNum[0]] = sheetInfor[i][twoNum[1]];
					sheetInfor[i][twoNum[1]] = temp;
				}
			}
		}

		updateDataBySheet(sheetInfor, wholeTeachers, ConstantVal.PROCESS_WHOLE);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Teacher> void updateDataBySheet(int sheetInfor[][], List<T> oneTypeTeacher, int type)
			throws Myexception {
		List targetTeacher = null;
		List<ProcessUtil> myDatas = new ArrayList<>();
		Map<Integer, Integer> convert = null;
		switch (type) {
		case ConstantVal.PROCESS_WHOLE:
			targetTeacher = (List<WholeTeacher>) oneTypeTeacher;
			for (WholeTeacher wt : (List<WholeTeacher>) targetTeacher) {
				myDatas.add(wt.wholePro);
			}
			convert = pgi2whole;
			break;
		case ConstantVal.PROCESS_EVEN:
			targetTeacher = (List<HalfTeacher>) oneTypeTeacher;
			for (HalfTeacher wt : (List<HalfTeacher>) targetTeacher) {
				myDatas.add(wt.evenUtil);
			}
			convert = pgi2half;
			break;
		case ConstantVal.PROCESS_ODD:
			targetTeacher = (List<HalfTeacher>) oneTypeTeacher;
			for (HalfTeacher wt : (List<HalfTeacher>) targetTeacher) {
				myDatas.add(wt.oddUtil);
			}
			convert = pgi2half;
			break;
		default:
			throw new Myexception(String.format("updateDataBySheet更新时异常，不支持处理%s类型", type));
		}

		for (ProcessUtil ad : myDatas) {
			ad.weekY.clear();
			ad.connectCells.clear();
			ad.conflictCells.clear();
			ad.arrangeCells.clear();
		}

		for (int i = 0; i < classNum; i++) {
			for (int j = 0; j < lessonNum * 7;) {
				if (everyWeek[j] == false) {
					int count = 1;
					int current = sheetInfor[i][j];
					if (current == -1)
						continue;
					Position temp = new Position(i, j);
					ProcessUtil focus = myDatas.get(convert.get(current));
					focus.connectCells.add(temp);
					while (count + j < lessonNum * 7) {
						if (current == sheetInfor[i][count + j]) {
							focus.connectCells.add(new Position(i, count + j));
							count++;
						} else {
							if (count == 1) {
								focus.connectCells.remove(temp);
							}
							break;
						}
					}
					if (count + j == lessonNum * 7 && count == 1) {
						focus.connectCells.remove(temp);
					}
					for (int k = 0; k < count; k++) {
						if (!focus.weekY.contains(j + k)) {
							focus.weekY.add(j + k);
						}
						focus.arrangeCells.add(new Position(i, j + k));
					}
					j += count;
				} else {
					j++;
				}

			}
		}

		for (int j = 0; j < lessonNum * 7; j++) {
			if (everyWeek[j] == false) {
				List<Integer> sameTime = new ArrayList<>();
				Set<Integer> oneRow = new HashSet<>();
				for (int i = 0; i < classNum; i++) {
					int index = sameTime.indexOf(sheetInfor[i][j]);
					if (index != -1) {
						oneRow.add(index);
						oneRow.add(i);
					}
					sameTime.add(sheetInfor[i][j]);

				}
				for (int k : oneRow) {
					myDatas.get(convert.get(sheetInfor[k][j])).conflictCells.add(new Position(k, j));
				}
			}

		}
		printSheet(sheetInfor);
	}

	public ResultType solve() throws Myexception {
		// printSheet(sheetInfor);

		copyGh(wholeTeachers, sheetInfor, bestResult);
		minCost = bestResult.getCost(definedCost);
		tempCostflu.add(minCost);
		tempCost = minCost;
		mycost = minCost;
		copyGh(wholeTeachers, sheetInfor, beginResult);
		int k = 0;// 降温次数
		int n = 0;// 迭代步数
		double t = t0;
		double r = 0.0;
		boolean connect = true;
		// boolean searchOnece = false;
		while (k < T) {
			n = 0;
			while (n < N) {
				copyGh(beginResult, tempResult);
				int index = isWholeConflict(tempResult.datas);
				if (index == -1) {

					int sumConnectNumber = 0;
					searchConnection(tempResult.sheetInfor, tempResult.datas, ConstantVal.PROCESS_WHOLE);
					ArrayList<WholeTeacher> myDatas = tempResult.datas;
					for (int i = 0; i < myDatas.size(); i++) {
						sumConnectNumber += myDatas.get(i).wholePro.connectCells.size();

					}
					if (sumConnectNumber == 0 || !connect) {
						randomChange(beginResult, tempResult);
					} else {
						try {
							// System.out.println("no conflict");
							connect = dealConnect(beginResult, tempResult, sumConnectNumber);
						} catch (Myexception e) {
							// TODO Auto-generated catch block
							randomChange(beginResult, tempResult);// 无法找出
							e.printMsg();
						}
					}

					noConflict = true;

				} else {
					noConflict = false;
					// try {
					dealConflict(beginResult, tempResult, index);
					// } catch (Myexception e) {
					// // TODO Auto-generated catch block
					// e.printMsg();
					// }
				}

				tempCost = tempResult.getCost(definedCost);
				tempCostflu.add(tempCost);
				if (noConflict == true && tempCost < minCost) {
					// printSheet(tempResult.sheetInfor);
					copyGh(tempResult.datas, tempResult.sheetInfor, bestResult);
					minCost = tempCost;
					bestCostflu.add(minCost);

				}
				r = random.nextDouble();
				if (tempCost < mycost || (tempCost != mycost && Math.exp((mycost - tempCost) / t) > r)) {
					copyGh(tempResult.datas, tempResult.sheetInfor, beginResult);
					mycost = tempCost;

				}
				n++;
			}
			t = a * t;
			k++;
		}

		// printConflict(beginResult.datas);
		// checkResult(bestResult);

		// System.out.println("tmp cost information");
		// for(int i=0;i<tempCostflu.size();i++){
		// if(i%100==0){
		// System.out.println();
		// }
		// System.out.print(tempCostflu.get(i)+"=>");
		// }
		// System.out.println();
		// SimpleDateFormat s = new SimpleDateFormat("YYYYMMdd-HHmmss");
		// String date = s.format(new Date());
		// new Thread(new WriteData(tempCostflu,
		// "C:\\SALog\\改进的sa\\tempcost-"+date+".txt")).start();
		// new Thread(new WriteData(bestCostflu,
		// "C:\\SALog\\改进的sa\\bestcost-"+date+".txt")).start();
		double runtime = System.currentTimeMillis() - Mytime.start;
		ArrayList<Double> parm = new ArrayList<>();
		parm.add(runtime);
		// new Thread(new WriteData(parm,
		// "C:\\SALog\\改进的sa\\runTime-"+date+".txt")).start();;
		if (bestCostflu.size() == 0) {
			logger.info("无解\r\n");
		} else {
			logger.info("best cost information:" + bestCostflu.size() + "    " + bestCostflu.get(bestCostflu.size() - 1)
					+ "\r\n");
			for (int i = 0; i < bestCostflu.size(); i++) {
				logger.info(bestCostflu.get(i) + "=>");
			}
		}
		if (noConflict == true) {
			int sum = 0;
			updateDataBySheet(bestResult.sheetInfor, bestResult.datas, ConstantVal.PROCESS_WHOLE);
			for (int p = 0; p < this.wholeTeachers.size(); p++) {
				if (this.wholeTeachers.get(p).wholePro.conflictCells.size() != 0) {
					sum += this.wholeTeachers.get(p).wholePro.conflictCells.size();
				}
			}
			logger.info("最后冲突: " + sum + "\n");
			sum = 0;
			for (int p = 0; p < this.wholeTeachers.size(); p++) {
				if (this.wholeTeachers.get(p).wholePro.connectCells.size() != 0) {
					sum += this.wholeTeachers.get(p).wholePro.connectCells.size();
				}
			}
			logger.info("最后连堂: " + sum + "\n");
			return bestResult;
		} else
			return null;
		// System.out.println("final cost= "+bestResult.getCost(definedCost));
		// printConflict(tempResult.datas);

	}

	public boolean checkResult(ResultType result) {
		logger.info("cheack result\n");
		int sheet[][] = result.sheetInfor;
		ArrayList<WholeTeacher> mydatdas = result.datas;
		ArrayList<Integer> teachIndex = new ArrayList<>();
		ArrayList<Integer> tNums = new ArrayList<>();
		int row = sheet.length;
		int col = sheet[0].length;

		for (int i = 0; i < row; i++) {
			teachIndex.clear();
			tNums.clear();
			for (int j = 0; j < col; j++) {
				int k = teachIndex.indexOf((Integer) sheet[i][j]);
				if (k == -1) {
					teachIndex.add(sheet[i][j]);
					tNums.add(1);
				} else {
					tNums.set(k, tNums.get(k) + 1);
				}
			}
			logger.info("第" + (i + 1) + "班信息：");

			for (int a = 0; a < tNums.size(); a++) {
				logger.info(mydatdas.get(teachIndex.get(a)).teacherName + "老师:" + tNums.get(a) + "节"
						+ mydatdas.get(teachIndex.get(a)).courseName + "课\n");
			}

		}
		return true;

	}

	public boolean isHalfConflict(){
		for(HalfTeacher teacher:halfTeachers){
			if(teacher.oddUtil.conflictCells.size()!=0||teacher.evenUtil.conflictCells.size()!=0){
				return true;
			}
		}
		return false;
	}
	
	public int isWholeConflict(ArrayList<WholeTeacher> result) {

		for (int i = 0; i < result.size(); i++) {
			if (result.get(i).wholePro.conflictCells.size() != 0)
				return i;
		}

		return -1;

	}

	public void copyGh(List<WholeTeacher> temp, int[][] sheetInfor, ResultType result) {
		result.datas = new ArrayList<>(temp);
		result.sheetInfor = new int[sheetInfor.length][sheetInfor[0].length];
		for (int i = 0; i < sheetInfor.length; i++) {
			for (int j = 0; j < sheetInfor[0].length; j++) {
				result.sheetInfor[i][j] = sheetInfor[i][j];

			}
		}

	}

	public void copyGh(ResultType begin, ResultType result) {
		result.datas = begin.datas;
		result.sheetInfor = begin.sheetInfor;

	}

	public void dealConflict(ResultType begin, ResultType result, int index) throws Myexception {

		copyGh(begin, result);
		ArrayList<WholeTeacher> datas = result.datas;
		WholeTeacher tData = result.datas.get(index);
		ArrayList<Position> tcon = tData.wholePro.conflictCells;
		ArrayList<Integer> lessonCon = new ArrayList<>();
		ArrayList<ArrayList<Integer>> classCon = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> oneclassCon = new ArrayList<>();
		lessonCon.add(tcon.get(0).timeY);
		oneclassCon.add(tcon.get(0).classX);
		int h = 0;
		for (int i = 1; i < tcon.size(); i++) {
			if (tcon.get(i).timeY == lessonCon.get(h)) {
				oneclassCon.add(tcon.get(i).classX);

			} else {
				lessonCon.add(tcon.get(i).timeY);
				classCon.add(new ArrayList<>(oneclassCon));
				oneclassCon.clear();
				oneclassCon.add(tcon.get(i).classX);
				h++;
			}

		}
		classCon.add(new ArrayList<>(oneclassCon));
		oneclassCon.clear();
		int sheet[][] = result.sheetInfor;
		for (int i = 0; i < classCon.size(); i++) {
			oneclassCon = classCon.get(i);// 同一个冲突课时包含的班级
			// ArrayList<Integer>everyClassExchange=new ArrayList<>();
			ArrayList<Position> exchangeY = new ArrayList<>();
			ArrayList<Double> suitable = new ArrayList<>();
			int onelessonCon = lessonCon.get(i);// 当前冲突的课时
			List<Boolean> permission = new ArrayList<>();
			for (int j = 0; j < oneclassCon.size(); j++) {

				ArrayList<Integer> allowedLesson = new ArrayList<>();
				int c = oneclassCon.get(j);
				// ArrayList<Integer> teachers = classIncludeTeacher.get(c);//
				// 获得每个冲突班级的老师
				if (fixTable[c][onelessonCon] == 0) {
					for (int x = 0; x < needLessons; x++) {
						if (everyWeek[x] == false) {
							if (!tData.wholePro.weekY.contains(x)) {
								if (fixTable[c][x] == 0) {
									if (!datas.get(sheet[c][x]).wholePro.weekY.contains(onelessonCon)) {
										allowedLesson.add(x);
									}
								}

							}
						}

					}

				}

				if (allowedLesson.size() != 0) {
					permission.add(true);
					ArrayList<Double> allowedCost = new ArrayList<>(allowedLesson.size());
					double repet = 600.0;

					for (int y : allowedLesson) {
						if (datas.get(sheet[c][y]).courseIndex == 3 && onelessonCon % lessonNum != lessonNum - 1) {
							allowedCost.add(1.0 / 99999);
							continue;
						}
						double tempNum = 1.0;

						try {
							if (sheet[c][y] == sheet[c][onelessonCon - 1]) {
								tempNum += repet;
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							if (sheet[c][y] == sheet[c][onelessonCon + 1]) {
								tempNum += repet;
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							if (sheet[c][onelessonCon] == sheet[c][y - 1]) {
								tempNum += repet;
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							if (sheet[c][onelessonCon] == sheet[c][y + 1]) {
								tempNum += repet;
							}
						} catch (Exception e) {
							// TODO: handle exception
						}

						allowedCost.add(1.0 / tempNum);
					}
					double sumSuit = 0;
					for (double each : allowedCost) {
						sumSuit += each;
					}
					double selectR = random.nextDouble() * sumSuit;
					double indual = 0;
					int site = -1;
					for (int n = 0; n < allowedCost.size(); n++) {
						indual += allowedCost.get(n);
						if (indual > selectR) {
							site = n;
							break;
						}
					}
					exchangeY.add(new Position(c, allowedLesson.get(site)));
					suitable.add(allowedCost.get(site));

				} else {
					permission.add(false);
					exchangeY.add(new Position(-1, -1));
					suitable.add(-1.0);
				}
			}
			int can = 0;
			for (boolean f : permission) {
				if (f) {
					can++;
				}
			}

			int myIndex = 0;// 最小的适应值，要保留不换的。

			int state = oneclassCon.size() - can;
			if (state == 0) {
				for (int x = 1; x < suitable.size(); x++) {
					if (suitable.get(x) < suitable.get(myIndex)) {
						myIndex = x;
					}
				}
			} else if (state == 1) {
				for (int x = 0; x < permission.size(); x++) {
					if (!permission.get(x)) {
						myIndex = x;
						break;
					}
				}
			} else {
				throw new Myexception("无解");
			}

			for (int x = 0; x < exchangeY.size(); x++) {
				if (x == myIndex) {
					continue;
				} else {
					Position cl = exchangeY.get(x);
					if (tcon.size() == 2) {
						tcon.clear();
					} else {
						int count = 0;
						for (Position tp : tcon) {
							if (tp.timeY == onelessonCon) {
								count++;
							}
						}
						if (count == 2) {
							tcon.clear();
						} else {
							for (Position tp : tcon) {
								if (tp.classX == cl.classX && tp.timeY == onelessonCon) {
									tcon.remove(tp);
									break;
								}
							}
						}
					}

					int t1 = sheet[cl.classX][onelessonCon], t2 = sheet[cl.classX][cl.timeY];
					logger.info("冲突交换：" + "(" + cl.classX + "," + cl.timeY + ")" + "<==>" + "(" + cl.classX + ","
							+ onelessonCon + ")\n");
					Position p1 = new Position(cl.classX, onelessonCon), p2 = new Position(cl.classX, cl.timeY);
					WholeTeacher a1 = result.datas.get(t1), a2 = result.datas.get(t2);
					for (Position tp : a1.wholePro.arrangeCells) {
						if (tp.classX == p1.classX && tp.timeY == p1.timeY) {
							a1.wholePro.arrangeCells.remove(tp);
							break;
						}
					}
					a1.wholePro.arrangeCells.add(p2);
					for (Position tp : a2.wholePro.arrangeCells) {
						if (tp.classX == p2.classX && tp.timeY == p2.timeY) {
							a2.wholePro.arrangeCells.remove(tp);
							break;
						}
					}
					a2.wholePro.arrangeCells.add(p1);
					if (!a1.wholePro.weekY.contains(cl.timeY)) {
						a1.wholePro.weekY.add(cl.timeY);
					}
					if (!a2.wholePro.weekY.contains(onelessonCon)) {
						a2.wholePro.weekY.add(onelessonCon);
					}

					int count = 0;
					for (Position tp : a2.wholePro.conflictCells) {
						if (tp.timeY == cl.timeY) {
							count++;
						}
					}

					if (count == 2 && a2.wholePro.conflictCells.size() == 2) {
						a2.wholePro.conflictCells.clear();
					} else if (count == 2 && a2.wholePro.conflictCells.size() != 2) {
						for (Position tp : a2.wholePro.conflictCells) {
							if (tp.timeY == onelessonCon) {
								a2.wholePro.conflictCells.remove(tp);

							}
						}
					} else if (count > 2) {
						for (Position tp : a2.wholePro.conflictCells) {
							if (tp.classX == cl.classX && tp.timeY == onelessonCon) {
								a2.wholePro.conflictCells.remove(tp);
								break;
							}
						}
					}

					sheet[cl.classX][onelessonCon] = t2;
					sheet[cl.classX][cl.timeY] = t1;

					int sum = 0;
					updateDataBySheet(sheet, wholeTeachers, ConstantVal.PROCESS_WHOLE);
					for (int p = 0; p < this.wholeTeachers.size(); p++) {
						if (this.wholeTeachers.get(p).wholePro.conflictCells.size() != 0) {
							sum += this.wholeTeachers.get(p).wholePro.conflictCells.size();
						}
					}
					logger.info("conflict: " + sum + "\n");
				}
			}

		}

	}

	public boolean dealConnect(ResultType begin, ResultType result, int sumConnectNumber) throws Myexception {

		copyGh(begin, result);
		// searchConnection(result);
		ArrayList<WholeTeacher> myDatas = result.datas;
		int sheet[][] = result.sheetInfor;

		// System.out.println("connection nunm=" + sumConnectNumber);
		int rdint = random.nextInt(sumConnectNumber);
		int tindex = 0;
		int tselct = 0;
		for (; tindex < myDatas.size(); tindex++) {
			tselct += myDatas.get(tindex).wholePro.connectCells.size();
			if (tselct > rdint) {
				break;
			}
		}
		WholeTeacher tData = myDatas.get(tindex);
		ArrayList<Position> tcon = tData.wholePro.connectCells;
		int rdindexy = random.nextInt(tcon.size());
		int classX = tcon.get(rdindexy).classX;
		int timeY = tcon.get(rdindexy).timeY;

		int top = rdindexy - 1, down = rdindexy + 1, count = 0, connectNum = 0;
		while (top >= 0) {
			if (tcon.get(top).classX == classX && tcon.get(top).timeY == timeY - count - 1) {
				top--;
				count++;
			} else {
				break;
			}

		}
		connectNum += count;
		int beginCon = rdindexy - count;
		count = 0;
		while (down < tcon.size()) {
			if (tcon.get(down).classX == classX && tcon.get(down).timeY == timeY + count + 1) {
				down++;
				count++;
			} else {
				break;
			}
		}
		connectNum += count + 1;
		int myCount = random.nextInt(connectNum);
		Position focusPosition = tcon.get(beginCon + myCount);
		for (int h = beginCon; h < beginCon + connectNum;) {
			if (fixTable[focusPosition.classX][focusPosition.timeY] == 0) {
				break;
			} else {

				focusPosition = tcon.get(h);
				h++;
			}
		}

		if (fixTable[focusPosition.classX][focusPosition.timeY] == 1) {
			boolean f = false;
			for (int i = 0; i < myDatas.size(); i++) {
				if (f) {
					break;
				}
				for (int j = 0; j < myDatas.get(i).wholePro.connectCells.size(); j++) {
					Position temp = myDatas.get(i).wholePro.connectCells.get(j);
					if (fixTable[temp.classX][temp.timeY] == 1) {
						continue;
					} else {
						f = true;
						focusPosition = temp;
						break;

					}
				}
			}
		}
		if (fixTable[focusPosition.classX][focusPosition.timeY] == 1) {
			return false;
		}
		int c = focusPosition.classX;
		timeY = focusPosition.timeY;
		ArrayList<Integer> allowed = new ArrayList<>();

		for (int i = 0; i < needLessons; i++) {
			if (everyWeek[i] == false) {
				if (!tData.wholePro.weekY.contains(i)) {
					if (fixTable[c][i] == 0) {
						if (!myDatas.get(sheet[c][i]).wholePro.weekY.contains(timeY)) {
							allowed.add(i);
						}
					}

				}
			}

		}

		if (allowed.size() != 0) {
			ArrayList<Double> allowedCost = new ArrayList<>(allowed.size());
			double repet = 99999.0;

			for (int y : allowed) {
				if (datas.get(sheet[c][y]).courseIndex == 3 && timeY % lessonNum != lessonNum - 1) {
					allowedCost.add(1.0 / 999999);
					continue;
				}
				double tempNum = 1.0;

				try {
					if (sheet[c][y] == sheet[c][timeY - 1]) {
						tempNum += repet;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					if (sheet[c][y] == sheet[c][timeY + 1]) {
						tempNum += repet;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					if (sheet[c][timeY] == sheet[c][y - 1]) {
						tempNum += repet;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					if (sheet[c][timeY] == sheet[c][y + 1]) {
						tempNum += repet;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				allowedCost.add(1.0 / tempNum);
			}
			int site = -1;
			int loop = 7;
			do {
				double sumSuit = 0;
				for (double each : allowedCost) {
					sumSuit += each;
				}
				double selectR = random.nextDouble() * sumSuit;
				double indual = 0;

				for (int n = 0; n < allowedCost.size(); n++) {
					indual += allowedCost.get(n);
					if (indual > selectR) {
						site = n;
						break;
					}
				}
				if (--loop < 0) {
					break;
				}
			} while (IsConnect(c, allowed.get(site), sheet, timeY));
			if (loop == -1 && IsConnect(c, allowed.get(site), sheet, timeY)) {

			} else {

				Position cl = new Position(c, allowed.get(site));

				// int myindex = 0;
				//
				// for (Position tp : tcon) {
				//
				// if (tp.timeY == timeY) {
				// // tcon.remove(tp);
				// break;
				// }
				// myindex++;
				// }
				//
				// try {
				// if (tcon.get(myindex + 1).timeY == timeY+1) {
				// tcon.remove(myindex + 1);
				// }
				// tcon.remove(myindex);
				// if (tcon.get(myindex - 1).timeY == timeY - 1) {
				// tcon.remove(myindex - 1);
				// }
				// } catch (Exception e) {
				// // TODO: handle exception
				// }

				int t1 = sheet[cl.classX][timeY], t2 = sheet[cl.classX][cl.timeY];
				logger.info("连堂交换：" + "(" + cl.classX + "," + cl.timeY + ")" + "<==>" + "(" + cl.classX + "," + timeY
						+ ")\n");
				Position p1 = new Position(cl.classX, timeY), p2 = new Position(cl.classX, cl.timeY);
				WholeTeacher a1 = result.datas.get(t1), a2 = result.datas.get(t2);
				for (Position tp : a1.wholePro.arrangeCells) {
					if (tp.classX == p1.classX && tp.timeY == p1.timeY) {
						a1.wholePro.arrangeCells.remove(tp);
						break;
					}
				}
				a1.wholePro.arrangeCells.add(p2);
				for (Position tp : a2.wholePro.arrangeCells) {
					if (tp.classX == p2.classX && tp.timeY == p2.timeY) {
						a2.wholePro.arrangeCells.remove(tp);
						break;
					}
				}
				a2.wholePro.arrangeCells.add(p1);

				Integer y1 = timeY;
				Integer y2 = cl.timeY;
				if (!a1.wholePro.weekY.contains(cl.timeY)) {
					a1.wholePro.weekY.add(cl.timeY);
				}
				a1.wholePro.weekY.remove(y1);
				if (!a2.wholePro.weekY.contains(timeY)) {
					a2.wholePro.weekY.add(timeY);
				}
				a2.wholePro.weekY.remove(y2);
				// myindex = 0;
				// for (Position tp : a2.connectLessons) {
				// if (tp.timeY == cl.timeY) {
				// break;
				// }
				// myindex++;
				// }
				// try {
				// if (a2.connectLessons.get(myindex + 1).timeY == timeY) {
				// a2.connectLessons.remove(myindex + 1);
				// }
				// a2.connectLessons.remove(myindex);
				// if (a2.connectLessons.get(myindex - 1).timeY == timeY -
				// 1) {
				// a2.connectLessons.remove(myindex - 1);
				// }
				// } catch (Exception e) {
				// // TODO: handle exception
				// }

				sheet[cl.classX][timeY] = t2;
				sheet[cl.classX][cl.timeY] = t1;

				int sum = 0;
				updateDataBySheet(sheet, wholeTeachers, ConstantVal.PROCESS_WHOLE);
				for (int p = 0; p < this.wholeTeachers.size(); p++) {
					if (this.wholeTeachers.get(p).wholePro.conflictCells.size() != 0) {
						sum += this.wholeTeachers.get(p).wholePro.conflictCells.size();
					}
				}
				logger.info("conflict: " + sum + "\n");
				sum = 0;
				for (int p = 0; p < this.wholeTeachers.size(); p++) {
					if (this.wholeTeachers.get(p).wholePro.connectCells.size() != 0) {
						sum += this.wholeTeachers.get(p).wholePro.connectCells.size();
					}
				}
				logger.info("connect: " + sum + "\n");
			}

		} else {
			// throw new Myexception("connection error");
			fixTable[focusPosition.classX][focusPosition.timeY] = 1;
		}
		return true;

	}

	public boolean IsConnect(int c, int site, int sheet[][], int timeY) {
		boolean flag = false;
		try {
			if (sheet[c][site] == sheet[c][timeY - 1] || sheet[c][site] == sheet[c][timeY + 1]) {
				flag = true;
			}
			if (sheet[c][timeY] == sheet[c][site - 1] || sheet[c][timeY] == sheet[c][site + 1]) {
				flag = true;
			}

		} catch (Exception e) {
			// TODO: handle exception
			try {
				if (sheet[c][timeY] == sheet[c][site - 1] || sheet[c][timeY] == sheet[c][site + 1]) {
					flag = true;
				}

			} catch (Exception e2) {
				// TODO: handle exception
			}

		}
		return flag;
	}

	public boolean checkVaild(ResultType result, int c1, int c2, int l1, int l2) {
		ArrayList<WholeTeacher> myDatas = result.datas;
		int sheet[][] = result.sheetInfor;
		int t1 = sheet[c1][l1];
		int t2 = sheet[c2][l2];

		if (fixTable[c1][l1] == 1) {
			return false;
		}
		if (fixTable[c2][l2] == 1) {
			return false;
		}

		if (t1 == t2) {
			return false;
		}

		if (myDatas.get(t1).wholePro.weekY.contains(l2)) {
			return false;
		}
		if (myDatas.get(t2).wholePro.weekY.contains(l1)) {
			return false;
		}

		if (l1 == 0) {
			if (sheet[c1][l1 + 1] == t2) {
				return false;
			}
		} else if (l1 == needLessons - 1) {
			if (sheet[c1][l1 - 1] == t2) {
				return false;
			}

		} else {
			if (sheet[c1][l1 - 1] == t2 || sheet[c1][l1 + 1] == t2) {
				return false;
			}
		}

		if (l2 == 0) {
			if (sheet[c2][l2 + 1] == t1) {
				return false;
			}
		} else if (l2 == needLessons - 1) {
			if (sheet[c2][l2 - 1] == t1) {
				return false;
			}

		} else {
			if (sheet[c2][l2 - 1] == t1 || sheet[c2][l2 + 1] == t1) {
				return false;
			}
		}

		if (myDatas.get(t1).courseIndex == 3 && l2 % lessonNum != lessonNum - 1) {
			return false;
		}

		if (myDatas.get(t2).courseIndex == 3 && l1 % lessonNum != lessonNum - 1) {
			return false;
		}

		return true;

	}

	public void randomChange(ResultType begin, ResultType result) {
		copyGh(begin, result);
		int sheet[][] = result.sheetInfor;

		int c1 = random.nextInt(classNum);
		int c2 = c1;

		int l1 = random.nextInt(needLessons);
		while (everyWeek[l1] || fixTable[c1][l1] == 1) {
			l1 = random.nextInt(needLessons);
		}
		int l2 = random.nextInt(needLessons);
		while (everyWeek[l2] || fixTable[c2][l2] == 1) {
			l2 = random.nextInt(needLessons);
		}

		while (everyWeek[l2] || l1 == l2) {
			l2 = random.nextInt(needLessons);
		}

		int loop = 70;
		boolean has = false;

		while (--loop > 0) {
			if (checkVaild(result, c1, c2, l1, l2)) {
				has = true;
				break;
			} else {
				l2 = random.nextInt(needLessons);
				while (everyWeek[l2] || l1 == l2) {
					l2 = random.nextInt(needLessons);
				}
			}

		}
		if (has) {
			int t1 = sheet[c1][l1];
			int t2 = sheet[c2][l2];

			logger.info("随机交换：" + "(" + c1 + "," + l1 + ")" + "<==>" + "(" + c2 + "," + l2 + ")" + "\r\n");
			WholeTeacher a1 = result.datas.get(t1), a2 = result.datas.get(t2);
			for (Position tp : a1.wholePro.arrangeCells) {
				if (tp.classX == c1 && tp.timeY == l1) {
					a1.wholePro.arrangeCells.remove(tp);
					break;
				}
			}
			a1.wholePro.arrangeCells.add(new Position(c2, l2));
			for (Position tp : a2.wholePro.arrangeCells) {
				if (tp.classX == c2 && tp.timeY == l2) {
					a2.wholePro.arrangeCells.remove(tp);
					break;
				}
			}
			a2.wholePro.arrangeCells.add(new Position(c1, l1));
			Integer y1 = l1;
			Integer y2 = l2;
			a1.wholePro.weekY.remove(y1);
			a1.wholePro.weekY.add(y2);
			a2.wholePro.weekY.remove(y2);
			a2.wholePro.weekY.add(y1);

			sheet[c1][l1] = t2;
			sheet[c2][l2] = t1;
		}

	}

}
