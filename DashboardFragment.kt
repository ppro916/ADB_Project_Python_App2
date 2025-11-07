package com.termux.controlcenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.termux.controlcenter.databinding.FragmentDashboardBinding
import com.termux.controlcenter.utils.SystemInfo

class DashboardFragment : Fragment() {
    
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var systemInfo: SystemInfo
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        systemInfo = SystemInfo(requireContext())
        setupDashboard()
        startSystemMonitoring()
    }
    
    private fun setupDashboard() {
        binding.cardUsb.setOnClickListener {
            (requireActivity() as MainActivity).showUsbFragment()
        }
        
        binding.cardStorage.setOnClickListener {
            (requireActivity() as MainActivity).showStorageFragment()
        }
        
        binding.cardNetwork.setOnClickListener {
            (requireActivity() as MainActivity).showNetworkFragment()
        }
        
        binding.cardMonitor.setOnClickListener {
            (requireActivity() as MainActivity).showMonitorFragment()
        }
    }
    
    private fun startSystemMonitoring() {
        updateSystemInfo()
        
        // Update every 2 seconds
        android.os.Handler().postDelayed({
            updateSystemInfo()
        }, 2000)
    }
    
    private fun updateSystemInfo() {
        // CPU Usage
        binding.cpuUsage.text = getString(R.string.cpu_usage_format, systemInfo.getCpuUsage())
        binding.cpuProgress.progress = systemInfo.getCpuUsage()
        
        // RAM Usage
        val ramUsage = systemInfo.getRamUsage()
        binding.ramUsage.text = getString(R.string.ram_usage_format, ramUsage.usagePercent)
        binding.ramProgress.progress = ramUsage.usagePercent
        binding.ramInfo.text = getString(
            R.string.ram_info_format, 
            ramUsage.usedMB, 
            ramUsage.totalMB
        )
        
        // Storage
        val storageInfo = systemInfo.getStorageInfo()
        binding.storageUsage.text = getString(
            R.string.storage_usage_format, 
            storageInfo.usedGB, 
            storageInfo.totalGB
        )
        binding.storageProgress.progress = storageInfo.usagePercent
        
        // Battery
        val batteryInfo = systemInfo.getBatteryInfo()
        binding.batteryLevel.text = getString(R.string.battery_format, batteryInfo.level)
        binding.batteryProgress.progress = batteryInfo.level
        binding.batteryStatus.text = batteryInfo.status
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
