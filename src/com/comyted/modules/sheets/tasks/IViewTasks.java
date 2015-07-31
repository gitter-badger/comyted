package com.comyted.modules.sheets.tasks;

import com.comyted.models.SheetDetails;
import com.comyted.models.TaskDetails;
import com.enterlib.app.IDataView;
import com.enterlib.app.IMessageReporter;

public interface IViewTasks extends IDataView, IMessageReporter {

	void showTaskEditView(TaskDetails task, SheetDetails sheet);
}
