package com.fortune.travels.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fortune.travels.model.ChangeType;
import com.fortune.travels.model.Expenses;
import com.fortune.travels.model.ExpensesReqRes;
import com.fortune.travels.repository.ExpensesDAOService;

@RestController
@RequestMapping("/fortune")
public class ExpensesController {
	
	@Autowired
	ExpensesDAOService expensesDAOService;
	
	@PostMapping("/expenses")
	public ExpensesReqRes expenses(@Valid @RequestBody ExpensesReqRes expensesReqRes) {
		ExpensesReqRes expensesResponse = new ExpensesReqRes();
		expensesResponse.setSucessful(true);
		try {
			if(null != expensesReqRes && null != expensesReqRes.getExpenses()
				&& 	expensesReqRes.getExpenses().size() > 0) {
				for(Expenses expense : expensesReqRes.getExpenses()) {
					if(expense.getChangeType() == ChangeType.INSERT) {
						expensesDAOService.insert(expense);
					}
					else if(expense.getChangeType() == ChangeType.UPDATE) {
						expensesDAOService.update(expense);
					}
					else if(expense.getChangeType() == ChangeType.REMOVE) {
						expensesDAOService.delete(expense);
					}
				}
			}
			expensesResponse.setFilterCriteria(expensesReqRes.getFilterCriteria());
			expensesResponse.setExpenses(expensesDAOService.selectAllExpenseCriteria(expensesReqRes.getFilterCriteria()));
		}
		catch(Exception e) {
			expensesResponse.setSucessful(false);
			expensesResponse.setErrorMessage(e.getMessage());
			return expensesResponse;
		}
		return expensesResponse;
	}
	
	@GetMapping("/getExpenses")
	public ExpensesReqRes getExpenses() {
		ExpensesReqRes expensesReqRes= new ExpensesReqRes(); 
		expensesReqRes.setExpenses(expensesDAOService.selectAllExpense());
		return expensesReqRes;
	}
}
