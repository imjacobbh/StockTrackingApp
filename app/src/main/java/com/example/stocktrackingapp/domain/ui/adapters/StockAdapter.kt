package com.example.stocktrackingapp.domain.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
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

    override fun getItemCount(): Int = listStocks.size

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(listStocks[position])
    }

    inner class StockViewHolder(private val binding: StockItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnLongClickListener {
                onClickStockItem.onLongClick(listStocks[layoutPosition])
                true
            }
        }

        fun bind(stock: StocksDomainModel) {
            with(binding) {
                textViewHigh.text = stock.high
                textViewSymbol.text = stock.symbol
                textViewLow.text = stock.low
                textViewPrice.text = stock.stockPrice
            }
            updateViewVisibility(stock)
        }

        private fun updateViewVisibility(stock: StocksDomainModel) {
            with(binding) {
                if (stock.stockPrice.isEmpty()) {
                    textViewPrice.visibility = View.GONE
                    lowLayout.visibility = View.GONE
                    highLayout.visibility = View.GONE
                    noInternetHigh.visibility = View.VISIBLE
                    noInternetLow.visibility = View.VISIBLE
                    noInternetPrice.visibility = View.VISIBLE
                } else {
                    textViewPrice.visibility = View.VISIBLE
                    lowLayout.visibility = View.VISIBLE
                    highLayout.visibility = View.VISIBLE
                    noInternetHigh.visibility = View.GONE
                    noInternetLow.visibility = View.GONE
                    noInternetPrice.visibility = View.GONE
                }
            }
        }
    }
}
