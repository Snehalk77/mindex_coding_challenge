package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Implementation of the ReportingStructureService interface
 *
 * @author      Snehal Karwa
 */
@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public ReportingStructure read(String employeeId) {
        LOG.debug("Reading ReportingStructure for id [{}]", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            LOG.debug("Invalid employeeId [{}]", employeeId);
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }

        int numReports = 0;
        Queue<Employee> q = new LinkedList<>();
        q.add(employee);
        while(q.size()!=0){
            Employee current = q.remove();
            List<Employee> currentDirectReports = current.getDirectReports();
            if(currentDirectReports!=null){
                numReports+=currentDirectReports.size();
                for(int i=0; i<currentDirectReports.size(); i++) {
                    Employee childEmployee = employeeRepository.findByEmployeeId(currentDirectReports.get(i).getEmployeeId());
                    q.add(childEmployee);
                }
            }
        }
        ReportingStructure newReportingStructure = new ReportingStructure(employee, numReports);

        return newReportingStructure;
    }
}
