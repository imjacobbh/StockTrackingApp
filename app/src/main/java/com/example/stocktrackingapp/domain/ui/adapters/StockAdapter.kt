package com.example.stocktrackingapp.domain.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stocktrackingapp.databinding.StockItemBinding
import com.example.stocktrackingapp.domain.model.StocksDomainModel

class StockAdapter(private val onClickStockItem: OnClickStockItem) :
    RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    private val listStocks: ArrayList<StocksDomainModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StockItemBinding.inflate(inflater, parent, false)
        return StockViewHolder(binding)
    }

    fun removeStock(stock: StocksDomainModel) {
        val index = listStocks.indexOf(stock)
        if (index != -1) {
            listStocks.removeAt(index)
            notifyItemRemoved(index)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setStocks(stocks: List<StocksDomainModel>) {
        listStocks.clear()
        listStocks.addAll(stocks)
        notifyDataSetChanged()
    }

    fun addStock(stock: StocksDomainModel) {
        listStocks.add(stock)
        notifyItemInserted(listStocks.size - 1)
    }

    fun updateStock(stock: StocksDomainModel) {
        val index = listStocks.indexOfFirst { it.symbol == stock.symbol }
        if (index != -1) {
            listStocks[index] = stock
            notifyItemChanged(index)
        }
    }

    override fun getItemCount(): Int = listStocks.size

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(listStocks[position])
    }

    inner class StockViewHolder(private val binding: StockItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stock: StocksDomainModel) {
            with(binding) {
                textViewHigh.text = stock.high
                textViewSymbol.text = stock.symbol
                textViewLow.text = stock.low
                textViewPrice.text = stock.stockPrice
            }

            itemView.setOnLongClickListener {
                onClickStockItem.onLongClick(stock)
                true
            }
        }
    }
}
