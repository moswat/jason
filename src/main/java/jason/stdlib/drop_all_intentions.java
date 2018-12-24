package jason.stdlib;

import java.util.Iterator;

import jason.asSemantics.Circumstance;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.Event;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

/**

  <p>Internal action: <b><code>.drop_all_intentions</code></b>.

  <p>Description: removes all intentions from the agent's set of
  intentions (even suspended intentions are removed).
  No event is produced.

  <p>This action changes the agent's circumstance structure by simply
  emptying the whole set of intentions (I), pending actions (PA),
  pending intentions (PI), and events in E that are not external
  events (thus generated by intentions).

  <p>Example:<ul>

  <li> <code>.drop_all_intentions</code>: all intentions except the one where .drop_all_ intentions appears are dropped.

  </ul>

  @see jason.stdlib.intend
  @see jason.stdlib.desire
  @see jason.stdlib.drop_all_desires
  @see jason.stdlib.drop_all_events
  @see jason.stdlib.drop_intention
  @see jason.stdlib.drop_desire
  @see jason.stdlib.succeed_goal
  @see jason.stdlib.fail_goal
  @see jason.stdlib.current_intention
  @see jason.stdlib.suspend
  @see jason.stdlib.suspended
  @see jason.stdlib.resume

 */
@Manual(
		literal=".drop_all_intentions",
		hint="removes all intentions from the agent's set of intentions",
		argsHint= {
				""
		},
		argsType= {
				""
		},
		examples= {
				".drop_all_events: all intentions except the one where .drop_all_ intentions appears are dropped"
		},
		seeAlso= {
				"jason.stdlib.intend",
				"jason.stdlib.desire",
				"jason.stdlib.drop_all_desires",
				"jason.stdlib.drop_all_events",
				"jason.stdlib.drop_event",
				"jason.stdlib.drop_intention",
				"jason.stdlib.drop_desire",
				"jason.stdlib.succeed_goal",
				"jason.stdlib.fail_goal",
				"jason.stdlib.current_intention",
				"jason.stdlib.resume",
				"jason.stdlib.suspend",
				"jason.stdlib.suspended"
		}
	)
@SuppressWarnings("serial")
public class drop_all_intentions extends DefaultInternalAction {

	@Override public int getMinArgs() {
        return 0;
    }
    @Override public int getMaxArgs() {
        return 0;
    }

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        checkArguments(args);

        Circumstance C = ts.getC();
        C.clearRunningIntentions();
        C.clearPendingIntentions();
        C.clearPendingActions();

        // drop intentions in E
        Iterator<Event> ie = C.getEventsPlusAtomic();
        while (ie.hasNext()) {
            Event e = ie.next();
            if (e.isInternal()) {
                C.removeEvent(e);
            }
        }

        // drop intentions in PE
        for (String ek: C.getPendingEvents().keySet()) {
            Event e = C.getPendingEvents().get(ek);
            if (e.isInternal()) {
                C.removePendingEvent(ek);
            }
        }

        // cancel future events generated by .at
        at atia = (at)ts.getAg().getIA(at.atAtom);
        atia.cancelAts();

        return true;
    }
}
