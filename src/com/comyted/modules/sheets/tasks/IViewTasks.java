package com.comyted.modules.sheets.tasks;

import com.comyted.models.SheetDetails;
import com.comyted.models.TaskDetails;
import com.enterlib.mvvm.IDataView;
import com.enterlib.mvvm.IMessageReporter;

public interface IViewTasks extends IDataView, IMessageReporter {

	void showTaskEditView(TaskDetails task, SheetDetails sheet);
}
