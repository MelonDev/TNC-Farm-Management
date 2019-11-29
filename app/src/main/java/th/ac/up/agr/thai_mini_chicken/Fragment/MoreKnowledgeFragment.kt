package th.ac.up.agr.thai_mini_chicken.Fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_knowledge_table.view.*
import th.ac.up.agr.thai_mini_chicken.Adapter.MoreKnowledgeAdapter
import th.ac.up.agr.thai_mini_chicken.Data.MoreKnowledgeData
import th.ac.up.agr.thai_mini_chicken.Data.MoreKnowledgeType
import th.ac.up.agr.thai_mini_chicken.HardData.ChickenHerb
import th.ac.up.agr.thai_mini_chicken.HardData.ChickenProgram

import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView


class MoreKnowledgeFragment : Fragment() {

    companion object {

        fun newInstance(type: MoreKnowledgeType): MoreKnowledgeFragment {
            val args = Bundle()
            args.putSerializable("TYPE", type)
            val fragment = MoreKnowledgeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_knowledge_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type: MoreKnowledgeType = arguments?.let {
            it.getSerializable("TYPE") as MoreKnowledgeType
        } ?: run {
            MoreKnowledgeType.UNKNOWN
        }


        var data: ArrayList<MoreKnowledgeData> = when (type) {
            MoreKnowledgeType.PROGRAM -> ChickenProgram().getArrayData()
            MoreKnowledgeType.HERB -> ChickenHerb().getArrayData()
            else -> ArrayList()
        }

        val recyclerView = QuickRecyclerView(context!!
                , view.knowledge_table_recycler_view
                , "linear"
                , 1
                , "vertical"
                , false
                , "alway"
                , "high")
                .recyclerView()

        recyclerView.adapter = MoreKnowledgeAdapter(this, type, data)
    }


}
