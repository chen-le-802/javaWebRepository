# 学生信息成绩查询

### 1. 查询所有学生的信息
```sql
SELECT * FROM student;
```

### 2. 查询所有课程的信息
```sql
SELECT * FROM course;
```

### 3. 查询所有学生的姓名、学号和班级
```sql
SELECT name, student_id, my_class FROM student;
```

### 4. 查询所有教师的姓名和职称
```sql
SELECT name, title FROM teacher;
```

### 5. 查询不同课程的平均分数
```sql
SELECT course_id, AVG(score) AS avg_score FROM score GROUP BY course_id;
```

### 6. 查询每个学生的平均分数
```sql
SELECT student_id, AVG(score) AS avg_score FROM score GROUP BY student_id;
```

### 7. 查询分数大于85分的学生学号和课程号
```sql
SELECT student_id, course_id FROM score WHERE score > 85;
```

### 8. 查询每门课程的选课人数
```sql
SELECT course_id, COUNT(*) AS num_of_students FROM score GROUP BY course_id;
```

### 9. 查询选修了"高等数学"课程的学生姓名和分数
```sql
SELECT name, score FROM student, score WHERE student.student_id = score.student_id AND course_id = 'C001';
```

### 10. 查询没有选修"大学物理"课程的学生姓名
```sql
SELECT name FROM student WHERE student_id NOT IN (SELECT student_id FROM score WHERE course_id = 'C002');
```

### 11. 查询C001比C002课程成绩高的学生信息及课程分数
```sql
SELECT student.*, sc1.score AS C001_score, sc2.score AS C002_score
FROM student
JOIN score sc1 ON student.student_id = sc1.student_id AND sc1.course_id = 'C001'
JOIN score sc2 ON student.student_id = sc2.student_id AND sc2.course_id = 'C002'
WHERE sc1.score > sc2.score;
```

### 12. 统计各科成绩各分数段人数：课程编号，课程名称，[100-85]，[85-70]，[70-60]，[60-0] 及所占百分比
```sql
SELECT
  c.course_id,
  c.course_name,
  COUNT(CASE WHEN s.score BETWEEN 85 AND 100 THEN 1 END) AS '100-85',
  COUNT(CASE WHEN s.score BETWEEN 70 AND 84.9 THEN 1 END) AS '85-70',
  COUNT(CASE WHEN s.score BETWEEN 60 AND 69.9 THEN 1 END) AS '70-60',
  COUNT(CASE WHEN s.score < 60 THEN 1 END) AS '60-0',
  (COUNT(CASE WHEN s.score BETWEEN 85 AND 100 THEN 1 END) / COUNT(*)) * 100 AS '% 100-85',
  (COUNT(CASE WHEN s.score BETWEEN 70 AND 84.9 THEN 1 END) / COUNT(*)) * 100 AS '% 85-70',
  (COUNT(CASE WHEN s.score BETWEEN 60 AND 69.9 THEN 1 END) / COUNT(*)) * 100 AS '% 70-60',
  (COUNT(CASE WHEN s.score < 60 THEN 1 END) / COUNT(*)) * 100 AS '% 60-0'
FROM course c
JOIN score s ON c.course_id = s.course_id
GROUP BY c.course_id, c.course_name;
```

### 13. 查询选择C002课程但没选择C004课程的成绩情况(不存在时显示为 null )
```sql
SELECT
  student.student_id,
  student.name,
  student.gender,
  student.birth_date,
  student.my_class,
  COALESCE(sc2.score, NULL) AS C002_score,
  NULL AS C004_score
FROM student
LEFT JOIN score sc2 ON student.student_id = sc2.student_id AND sc2.course_id = 'C002'
LEFT JOIN score sc4 ON student.student_id = sc4.student_id AND sc4.course_id = 'C004'
WHERE sc2.course_id IS NOT NULL AND sc4.course_id IS NULL;
```

### 14. 查询平均分数最高的学生姓名和平均分数
```sql
SELECT student.name, AVG(score.score) AS average_score
FROM student
JOIN score ON student.student_id = score.student_id
GROUP BY student.student_id
ORDER BY average_score DESC
LIMIT 1;
```

### 15. 查询总分最高的前三名学生的姓名和总分
```sql
SELECT
  s.name,
  SUM(scr.score) AS total_score
FROM student s
JOIN score scr ON s.student_id = scr.student_id
GROUP BY s.student_id, s.name
ORDER BY total_score DESC
LIMIT 3;
```

### 16. 查询各科成绩最高分、最低分和平均分
要求如下：
- 以如下形式显示：课程 ID，课程 name，最高分，最低分，平均分，及格率，中等率，优良率，优秀率
- 及格为>=60，中等为：70-80，优良为：80-90，优秀为：>=90
- 要求输出课程号和选修人数，查询结果按人数降序排列，若人数相同，按课程号升序排列
```sql
SELECT
  c.course_id,
  c.course_name,
  MAX(s.score) AS max_score,
  MIN(s.score) AS min_score,
  AVG(s.score) AS avg_score,
  COUNT(*) AS num_students,
  (SUM(CASE WHEN s.score >= 60 THEN 1 ELSE 0 END) / COUNT(*)) * 100 AS pass_rate,
  (SUM(CASE WHEN s.score BETWEEN 70 AND 80 THEN 1 ELSE 0 END) / COUNT(*)) * 100 AS moderate_rate,
  (SUM(CASE WHEN s.score BETWEEN 80 AND 90 THEN 1 ELSE 0 END) / COUNT(*)) * 100 AS good_rate,
  (SUM(CASE WHEN s.score >= 90 THEN 1 ELSE 0 END) / COUNT(*)) * 100 AS excellent_rate
FROM course c
JOIN score s ON c.course_id = s.course_id
GROUP BY c.course_id, c.course_name
ORDER BY num_students DESC, c.course_id ASC;
```

### 17. 查询男生和女生的人数
```sql
SELECT gender, COUNT(*) AS count
FROM student
GROUP BY gender;
```

### 18. 查询年龄最大的学生姓名
```sql
SELECT name
FROM student
WHERE birth_date = (SELECT MIN(birth_date) FROM student);
```

### 19. 查询年龄最小的教师姓名
```sql
SELECT name
FROM teacher
WHERE birth_date = (SELECT MAX(birth_date) FROM teacher);
```

### 20. 查询学过「张教授」授课的同学的信息
```sql
SELECT DISTINCT s.*
FROM student s
JOIN score sc ON s.student_id = sc.student_id
JOIN course c ON sc.course_id = c.course_id
JOIN teacher t ON c.teacher_id = t.teacher_id
WHERE t.name = '张教授';
```

### 21. 查询至少有一门课与学号为"2021001"的同学所学相同的同学的信息
```sql
SELECT DISTINCT s.*
FROM student s
JOIN score sc ON s.student_id = sc.student_id
WHERE sc.course_id IN (
  SELECT course_id
  FROM score
  WHERE student_id = '2021001'
) AND s.student_id != '2021001';
```

### 22. 查询每门课程的平均分数，并按平均分数降序排列
```sql
SELECT
  c.course_id,
  c.course_name,
  AVG(sc.score) AS average_score
FROM course c
JOIN score sc ON c.course_id = sc.course_id
GROUP BY c.course_id, c.course_name
ORDER BY average_score DESC;
```

### 23. 查询学号为"2021001"的学生所有课程的分数
```sql
SELECT
  c.course_id,
  c.course_name,
  sc.score
FROM score sc
JOIN course c ON sc.course_id = c.course_id
WHERE sc.student_id = '2021001';
```

### 24. 查询所有学生的姓名、选修的课程名称和分数
```sql
SELECT
  s.name AS student_name,
  c.course_name,
  sc.score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
JOIN course c ON sc.course_id = c.course_id;
```

### 25. 查询每个教师所教授课程的平均分数
```sql
SELECT
  t.teacher_id,
  t.name AS teacher_name,
  AVG(sc.score) AS average_score
FROM teacher t
JOIN course c ON t.teacher_id = c.teacher_id
JOIN score sc ON c.course_id = sc.course_id
GROUP BY t.teacher_id, t.name;
```

### 26. 查询分数在80到90之间的学生姓名和课程名称
```sql
SELECT
  s.name AS student_name,
  c.course_name
FROM student s
JOIN score sc ON s.student_id = sc.student_id
JOIN course c ON sc.course_id = c.course_id
WHERE sc.score BETWEEN 80 AND 90;
```

### 27. 查询每个班级的平均分数
```sql
SELECT
  s.my_class,
  AVG(sc.score) AS average_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.my_class;
```

### 28. 查询没学过"王讲师"老师讲授的任一门课程的学生姓名
```sql
SELECT name
FROM student
WHERE student_id NOT IN (
  SELECT DISTINCT sc.student_id
  FROM score sc
  JOIN course c ON sc.course_id = c.course_id
  JOIN teacher t ON c.teacher_id = t.teacher_id
  WHERE t.name = '王讲师'
);
```

### 29. 查询两门及其以上小于85分的同学的学号，姓名及其平均成绩
```sql
SELECT
  s.student_id,
  s.name,
  AVG(sc.score) AS average_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
WHERE sc.score < 85
GROUP BY s.student_id, s.name
HAVING COUNT(*) >= 2;
```

### 30. 查询所有学生的总分并按降序排列
```sql
SELECT
  s.student_id,
  s.name,
  SUM(sc.score) AS total_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.student_id, s.name
ORDER BY total_score DESC;
```

### 31. 查询平均分数超过85分的课程名称
```sql
SELECT
  c.course_name
FROM course c
JOIN score s ON c.course_id = s.course_id
GROUP BY c.course_id, c.course_name
HAVING AVG(s.score) > 85;
```

### 32. 查询每门课程分数最高的学生姓名和分数
```sql
SELECT
  c.course_name,
  s.name AS student_name,
  MAX(sc.score) AS max_score
FROM course c
JOIN score sc ON c.course_id = sc.course_id
JOIN student s ON sc.student_id = s.student_id
GROUP BY c.course_id, c.course_name
ORDER BY c.course_name;
```

### 33. 查询选修了"高等数学"和"大学物理"的学生姓名
```sql
SELECT
  s.name
FROM student s
JOIN score sc1 ON s.student_id = sc1.student_id AND sc1.course_id = 'C001'
JOIN score sc2 ON s.student_id = sc2.student_id AND sc2.course_id = 'C002';
```

### 34. 按平均成绩从高到低显示所有学生的所有课程的成绩以及平均成绩（没有选课则为空）
```sql
WITH StudentScores AS (
  SELECT
    s.student_id,
    s.name,
    c.course_name,
    sc.score,
    AVG(sc.score) OVER (PARTITION BY s.student_id) AS avg_score
  FROM student s
  LEFT JOIN score sc ON s.student_id = sc.student_id
  LEFT JOIN course c ON sc.course_id = c.course_id
)
SELECT *
FROM StudentScores
ORDER BY avg_score DESC, student_id, course_name;
```

### 35. 查询分数最高和最低的学生姓名及其分数
```sql
WITH ScoreExtremes AS (
  SELECT
    s.student_id,
    s.name,
    sc.score,
    RANK() OVER (ORDER BY sc.score DESC) AS highest_rank,
    RANK() OVER (ORDER BY sc.score ASC) AS lowest_rank
  FROM student s
  JOIN score sc ON s.student_id = sc.student_id
)
SELECT name, score
FROM ScoreExtremes
WHERE highest_rank = 1 OR lowest_rank = 1;
```

### 36. 查询每个班级的最高分和最低分
```sql
SELECT
  s.my_class,
  MAX(sc.score) AS max_score,
  MIN(sc.score) AS min_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.my_class;
```

### 37. 查询每门课程的优秀率（优秀为90分）
```sql
SELECT
  c.course_name,
  (COUNT(CASE WHEN sc.score >= 90 THEN 1 END) / COUNT(*)) * 100 AS excellence_rate
FROM course c
JOIN score sc ON c.course_id = sc.course_id
GROUP BY c.course_name;
```

### 38. 查询平均分数超过班级平均分数的学生
```sql
WITH ClassAverages AS (
  SELECT
    s.my_class,
    AVG(sc.score) AS class_avg
  FROM student s
  JOIN score sc ON s.student_id = sc.student_id
  GROUP BY s.my_class
),
StudentAverages AS (
  SELECT
    s.student_id,
    s.name,
    AVG(sc.score) AS student_avg,
    s.my_class
  FROM student s
  JOIN score sc ON s.student_id = sc.student_id
  GROUP BY s.student_id, s.name, s.my_class
)
SELECT
  sa.student_id,
  sa.name,
  sa.student_avg
FROM StudentAverages sa
JOIN ClassAverages ca ON sa.my_class = ca.my_class
WHERE sa.student_avg > ca.class_avg;
```

### 39. 查询每个学生的分数及其与课程平均分的差值
```sql
WITH CourseAverages AS (
  SELECT
    c.course_id,
    AVG(sc.score) AS course_avg
  FROM course c
  JOIN score sc ON c.course_id = sc.course_id
  GROUP BY c.course_id
)
SELECT
  s.student_id,
  s.name,
  c.course_name,
  sc.score,
  ca.course_avg,
  (sc.score - ca.course_avg) AS diff
FROM student s
JOIN score sc ON s.student_id = sc.student_id
JOIN course c ON sc.course_id = c.course_id
JOIN CourseAverages ca ON c.course_id = ca.course_id;
```

### 40. 查询至少有一门课程分数低于80分的学生姓名
```sql
SELECT DISTINCT
  s.name
FROM student s
JOIN score sc ON s.student_id = sc.student_id
WHERE sc.score < 80;
```

### 41. 查询所有课程分数都高于85分的学生姓名
```sql
SELECT
  s.name
FROM student s
WHERE NOT EXISTS (
  SELECT 1
  FROM score sc
  WHERE sc.student_id = s.student_id AND sc.score <= 85
);
```

### 42. 查询平均成绩大于等于90分的同学的学生编号和学生姓名和平均成绩
```sql
SELECT
  s.student_id,
  s.name,
  AVG(sc.score) AS average_score
FROM student s
JOIN score sc ON s.student_id = sc.student_id
GROUP BY s.student_id, s.name
HAVING AVG(sc.score) >= 90;
```
