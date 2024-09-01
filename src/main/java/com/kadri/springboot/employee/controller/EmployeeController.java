package com.kadri.springboot.employee.controller;

import com.kadri.springboot.employee.Exceptions.EmployeeUpdateException;
import com.kadri.springboot.employee.entity.Employee;
import com.kadri.springboot.employee.entity.Task;
import com.kadri.springboot.employee.service.EmployeeService;
import com.kadri.springboot.employee.service.TaskService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller

public class EmployeeController {
    private EmployeeService employeeService;
    private TaskService taskService;


    @Autowired // since we have just one constructer Autowired is optional
    public EmployeeController(EmployeeService theEmployeeService, TaskService taskService) {
        this.employeeService = theEmployeeService;
        this.taskService = taskService;
    }

    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage() {

        return "fancy-login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {

        return "access-denied";
    }


    @GetMapping("/list")
    public String listEmployees(Model theModel) {
        try {
            List<Employee> theEmployees = employeeService.findAll();
            theModel.addAttribute("employees", theEmployees);
        } catch (Exception ex) {
            // Log the exception or handle it as needed
            ex.printStackTrace();
            // Optionally, you can add an error message attribute to the model
            theModel.addAttribute("errorMessage", "An unexpected error occurred while fetching employee data.");
            // Return the error view
            return "error-page"; // Assuming you have an error view named "error.html"
        }
        return "list-employees";
    }


    @RequestMapping("/addEmployee")
    public String addEmployee(Model theModel) {
        try {
            theModel.addAttribute("employee", new Employee());
        } catch (Exception ex) {
            // Log the exception or handle it as needed
            ex.printStackTrace();
            // Optionally, you can add an error message attribute to the model
            theModel.addAttribute("errorMessage", "An unexpected error occurred while adding an employee.");
            // Return the error view
            return "error"; // Assuming you have an error view named "error.html"
        }
        return "add-employee";
    }

    @GetMapping("/employee/{employeeId}/addtask")
    public String addTask(@PathVariable int employeeId, Model model) {
        // Implement logic to fetch the task by ID and add it to the model
        Task task = new Task();
        Employee employee = employeeService.findById(employeeId);
        model.addAttribute("employee", employee);
        model.addAttribute("task", task);

        return "task-add-form"; // Return the name of the edit task form template
    }

    @PostMapping("/employee/{employeeId}/save") // here i´ve updated findById to findByIdwithTasks(join fetch)
    public String saveNewTask(@RequestParam("employeeId") int employeeId, @ModelAttribute("task") Task addedTask) {
        // Implement logic to delete the task by ID
        Employee employee = employeeService.findById(employeeId);
        /*
        addedTask.setEmployee(employee);
        taskService.save(addedTask);
        addedTask.setEmployee(employee);*/
        employee.add(addedTask,employee);
        employeeService.update(employee);
        return "redirect:/employee/" + employeeId + "/tasks"; // Redirect to the employee tasks page
    }

    @GetMapping("/employee/{id}/tasks")
    public String showTasks(@PathVariable("id") int employeeId, HttpServletRequest request, Model model) {
        Employee theEmployee = employeeService.findById(employeeId);

        if (theEmployee == null) {
            // Handle case where employee is not found
            return "employee-not-found";
        }

        // Get the list of tasks associated with the employee
        List<Task> tasks = theEmployee.getTasks();

        // Add the employee and tasks to the model
        model.addAttribute("employee", theEmployee);
        model.addAttribute("tasks", tasks);

        // Return the name of the template to render
        return "employee-tasks";
    }

    @GetMapping("/employee/{employeeId}/task/{taskId}/edit")
    public String editTask(@PathVariable int employeeId, @PathVariable Long taskId, Model model) {
        // Implement logic to fetch the task by ID and add it to the model
        Task task = taskService.findById(taskId);
        Employee employee = employeeService.findById(employeeId);
        model.addAttribute("employee", employee);
        model.addAttribute("task", task);

        return "task-edit-form"; // Return the name of the edit task form template
    }

    // Controller method for deleting a task
    @GetMapping("/employee/{employeeId}/task/{taskId}/delete")
    public String deleteTask(@PathVariable int employeeId, @PathVariable Long taskId) {
        // Implement logic to delete the task by ID
        Task task = taskService.findById(taskId);
        Employee employee = employeeService.findById(employeeId);
        employee.setTasks(null);

        taskService.delete(task);
        return "redirect:/employee/" + employeeId + "/tasks"; // Redirect to the employee tasks page
    }

    @PostMapping("/employee/{employeeId}/task/{taskId}/save")
    public String saveTask(@RequestParam("employeeId") int employeeId, @RequestParam("taskId") Long taskId, @ModelAttribute("task") Task updatedTask) {
        // Implement logic to delete the task by ID
        Task task = taskService.findById(taskId);
        Employee employee = employeeService.findById(employeeId);
        updatedTask.setId((long) taskId);
        updatedTask.setReport(task.getReport());
        updatedTask.setFileName(task.getFileName());
        updatedTask.setEmployee(employee);
        taskService.update(updatedTask);
        return "redirect:/employee/" + employeeId + "/tasks"; // Redirect to the employee tasks page
    }


    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute Employee addemployee, Model theModel) {


        try {


            employeeService.save(addemployee);

            return "redirect:/list";
        } catch (Exception e) {
            throw new EmployeeUpdateException("Error occurred while Adding the employee: " + e.getMessage(), "SAVE_ERROR");
        }
    }

    @PostMapping("/updateEmployee/{employeeId}")
    public String updateEmployee(@ModelAttribute Employee employee,
                                 @PathVariable int employeeId,
                                 HttpServletResponse response,
                                 Model theModel) {

        employee.setId(employeeId);
        employeeService.update(employee);
        return "redirect:/list";

    }

    @GetMapping("/showFormForUpdate/{employeeId}")
    public String showFormForUpdate(@PathVariable("employeeId") int employeeId, HttpServletRequest request,

                                    Model theModel) {
        try {
            Employee employee = employeeService.findById(employeeId);

            theModel.addAttribute("employee", employee);
            return "update-employee";
        } catch (Exception e) {
            String errorCode = Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()); // Default to 500 if not available
            if (request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) != null) {
                errorCode = Integer.toString((int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
            }
            throw new EmployeeUpdateException("Employee not found: " + e.getMessage(), errorCode);
        }
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("employeeId") int theId, HttpServletRequest request, Model theModel) {
        try {
            Employee theemployee = employeeService.findById(theId);

            employeeService.delete(theemployee);
            return "redirect:/list";
        } catch (Exception e) {
            String errorCode = Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()); // Default to 500 if not available
            if (request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) != null) {
                errorCode = Integer.toString((int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
            }
            throw new EmployeeUpdateException("Employee not found: " + e.getMessage(), errorCode);
        }

    }

    @GetMapping("/employee/{employeeId}/task/{taskId}/deleteReport")
    public String deleteReport(@PathVariable int employeeId, @PathVariable Long taskId, HttpServletRequest request, Model theModel) {
        try {
            Employee theemployee = employeeService.findById(employeeId);
            System.out.println("im here" + taskId);
            System.out.println("task list" + theemployee.getTasks());
            for(Task task : theemployee.getTasks())
            {
                if (task.getId().equals(taskId)) {
                    System.out.println("im here" + taskId);
                    task.setReport(null);
                    task.setFileName(null);
                    task.setEmployee(theemployee);
                    taskService.save(task);
                    return "redirect:/employee/" + employeeId + "/tasks";
                }

            }
            return "error-page";

        } catch (Exception e) {
            String errorCode = Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()); // Default to 500 if not available
            if (request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) != null) {
                errorCode = Integer.toString((int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
            }
            throw new EmployeeUpdateException("The file is not found: " + e.getMessage(), errorCode);
        }

    }

    @RequestMapping("/employee/{employeeId}/task/{taskId}/uploadReport")
    public String uploadReport(@PathVariable int employeeId, @PathVariable Long taskId,
                               @RequestParam("reportFile") MultipartFile reportFile,
                               Model theModel) {
        try {
            // Find the employee by ID
            Employee employee = employeeService.findById(employeeId);

            // Check if a report file is uploaded
            if (employee != null && employee.getTasks() != null && !reportFile.isEmpty()) {
                System.out.println("inside the if ");
                // Convert the report file to bytes
                for (Task task : employee.getTasks()) {
                    if (task.getId().equals(taskId)) { // Assuming taskId is the ID of the task
                        // Convert the report file to bytes

                        byte[] reportBytes = reportFile.getBytes();
                        System.out.println("Report File: " + Arrays.toString(reportBytes));

                        task.setReport(reportBytes);
                        task.setFileName(reportFile.getOriginalFilename());
                        task.setEmployee(employee);

                        taskService.save(task);
                        break; // Exit the loop once the task is found
                    }
                }

            }

            return "redirect:/employee/" + employeeId + "/tasks";


        } catch (Exception e) {
            // Handle exceptions
            theModel.addAttribute("errorMessage", "Error occurred while uploading the report: " + e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/employee/{employeeId}/task/{taskId}/downloadReport")
    public ResponseEntity<byte[]> downloadReport(@PathVariable("employeeId") int employeeId, @PathVariable("taskId") Long taskId, Model model) {
        // Retrieve the task by ID
        Employee employee = employeeService.findById(employeeId);

        for (Task task : employee.getTasks()) {
            if (task.getId().equals(taskId)) {
                // Prepare HttpHeaders to indicate content type and disposition
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDisposition(ContentDisposition.builder("attachment")
                        .filename(task.getFileName()).build());

                // Return the report bytes with appropriate headers
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(task.getReport());
            }
        }

        // If the task with the given ID is not found
        return ResponseEntity.notFound().build();
    }
}



