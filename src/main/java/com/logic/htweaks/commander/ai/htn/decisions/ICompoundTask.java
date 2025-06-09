package com.logic.htweaks.commander.ai.htn.decisions;

import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.commander.ai.htn.actions.AbstractAction;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public interface ICompoundTask {
     abstract ObjectArrayList<AbstractAction> getActions(MiltaryAiCommander commander);
}
