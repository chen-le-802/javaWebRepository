# 员工信息练习题

### 1. 查询所有员工的姓名、邮箱和工作岗位。
```sql
SELECT last_name, first_name, email, job_title FROM employees;
```

### 2. 查询所有部门的名称和位置。
```sql
SELECT dept_name, location FROM departments;
```

### 3. 查询工资超过70000的员工姓名和工资。
```sql
SELECT last_name, first_name, salary FROM employees WHERE salary > 70000;
```

### 4. 查询IT部门的所有员工。
```sql
SELECT *
FROM employees
WHERE dept_id = (SELECT dept_id FROM departments WHERE dept_name = 'IT');
```

### 5. 查询入职日期在2020年之后的员工信息。
```sql
SELECT * FROM employees WHERE hire_date > '2020-01-01';
```

### 6. 计算每个部门的平均工资。
```sql
SELECT dept_name, AVG(salary) AS avg_salary
FROM employees JOIN departments ON employees.dept_id = departments.dept_id
GROUP BY dept_name;
```

### 7. 查询工资最高的前3名员工信息。
```sql
SELECT * FROM employees ORDER BY salary DESC LIMIT 3;
```

### 8. 查询每个部门员工数量。
```sql
SELECT dept_id, COUNT(*) AS emp_count
FROM employees
GROUP BY dept_id;
```

### 9. 查询没有分配部门的员工。
```sql
SELECT * FROM employees WHERE dept_id IS NULL;
```

### 10. 查询参与项目数量最多的员工。
```sql
SELECT employees.emp_id AS emp_id, last_name, first_name, COUNT(project_id) AS proj_count
FROM employees JOIN employee_projects ON employees.emp_id = employee_projects.emp_id
GROUP BY employees.emp_id
ORDER BY proj_count DESC
LIMIT 1;
```

### 11. 计算所有员工的工资总和。
```sql
SELECT SUM(salary) AS total_salary FROM employees;
```

### 12. 查询姓"Smith"的员工信息。
```sql
SELECT * FROM employees WHERE last_name = 'Smith';
```

### 13. 查询即将在半年内到期的项目。
```sql
SELECT * FROM projects WHERE end_date < DATE_ADD(NOW(), INTERVAL 6 MONTH);
```

### 14. 查询至少参与了两个项目的员工。
```sql
SELECT last_name, first_name, COUNT(project_id) AS proj_count
FROM employees JOIN employee_projects ON employees.emp_id = employee_projects.emp_id
GROUP BY employees.emp_id
HAVING proj_count >= 2;
```

### 15. 查询没有参与任何项目的员工。
```sql
SELECT * FROM employees WHERE emp_id NOT IN (
  SELECT emp_id FROM employee_projects
);
```

### 16. 计算每个项目参与的员工数量。
```sql
SELECT projects.project_id AS project_id, project_name, COUNT(emp_id) AS emp_count
FROM projects LEFT JOIN employee_projects ON employee_projects.project_id = projects.project_id
GROUP BY projects.project_id;
```

### 17. 查询工资第二高的员工信息。
```sql
WITH ranked_salaries AS (
  SELECT employees.*, RANK() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS salary_rank
  FROM employees
)
SELECT last_name, first_name, salary
FROM ranked_salaries
WHERE salary_rank = 2;
```

### 18. 查询每个部门工资最高的员工。
```sql
WITH ranked_salaries AS (
  SELECT employees.dept_id AS dept_id, dept_name, last_name, first_name, salary,
         RANK() OVER (PARTITION BY employees.dept_id ORDER BY salary DESC) AS department_salary_rank
  FROM employees JOIN departments ON employees.dept_id = departments.dept_id
)
SELECT *
FROM ranked_salaries
WHERE department_salary_rank = 1;
```

### 19. 计算每个部门的工资总和,并按照工资总和降序排列。
```sql
WITH dept_salaries AS (
  SELECT dept_id, SUM(salary) AS dept_total_salary
  FROM employees
  GROUP BY dept_id
)
SELECT dept_id, dept_total_salary,
       RANK() OVER (ORDER BY dept_total_salary DESC) AS dept_salary_rank
FROM dept_salaries;
```

### 20. 查询员工姓名、部门名称和工资。
```sql
SELECT last_name, first_name, dept_name, salary
FROM employees JOIN departments ON employees.dept_id = departments.dept_id;
```

### 21. 查询每个员工的上级主管(假设emp_id小的是上级)。
```sql
SELECT emp_id, last_name, first_name, (emp_id - 1) AS manager_id
FROM employees
WHERE (emp_id - 1) IN (
  SELECT emp_id
  FROM employees
);
```

### 22. 查询所有员工的工作岗位,不要重复。
```sql
SELECT DISTINCT job_title FROM employees;
```

### 23. 查询平均工资最高的部门。
```sql
WITH dept_salaries AS (
  SELECT dept_name, AVG(salary) AS avg_salary
  FROM employees JOIN departments ON employees.dept_id = departments.dept_id
  GROUP BY dept_name
), avg_salary_rank AS (
  SELECT dept_name, avg_salary,
         RANK() OVER (ORDER BY avg_salary DESC) AS avg_salary_rank
  FROM dept_salaries
)
SELECT dept_name, avg_salary, avg_salary_rank
FROM avg_salary_rank
WHERE avg_salary_rank = 1;
```

### 24. 查询工资高于其所在部门平均工资的员工。
```sql
SELECT last_name, first_name, salary, dept_name
FROM employees JOIN departments ON employees.dept_id = departments.dept_id
WHERE salary > (SELECT AVG(salary) FROM employees WHERE dept_id = employees.dept_id);
```

### 25. 查询每个部门工资前两名的员工。
```sql
WITH dept_salaries AS (
  SELECT last_name, first_name, dept_id, salary,
         RANK() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS salary_rank
  FROM employees
)
SELECT dept_id, salary, last_name, first_name, salary_rank
FROM dept_salaries
WHERE salary_rank <= 2;
```
